package ru.genby.jokebot;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.genby.jokebot.config.BotConfig;
import ru.genby.jokebot.infrastructure.BotCommands;
import ru.genby.jokebot.infrastructure.BotStateContext;
import ru.genby.jokebot.infrastructure.ChatCommandsCache;
import java.util.*;

@Slf4j
@Component
@FieldDefaults(makeFinal = true)
public class TelegramJokeBot extends TelegramLongPollingBot {
    private BotStateContext botStateContext;
//    private JokeService jokeService; //для записи в БД
    private ChatCommandsCache chatCommandsCache;
    BotConfig config;

    private HashMap<String, BotCommands> caseHandlers = new HashMap<>() {{
        put(BotCommands.START.getCommand(), BotCommands.START);
        put((BotCommands.RANDOM.getCommand()), BotCommands.RANDOM);
        put((BotCommands.SPY.getCommand()), BotCommands.SPY);
        put((BotCommands.PETKA.getCommand()), BotCommands.PETKA);
        put((BotCommands.ANIMAL.getCommand()), BotCommands.ANIMAL);
        put((BotCommands.MORE.getCommand()), BotCommands.MORE);
    }};

    public TelegramJokeBot(BotStateContext botStateContext, ChatCommandsCache chatCommandsCache, BotConfig config) {
        this.botStateContext = botStateContext;
        this.chatCommandsCache = chatCommandsCache;
        this.config = config;
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand(BotCommands.START.getCommand(), "Запуск"));
        botCommands.add(new BotCommand(BotCommands.RANDOM.getCommand(), "Случайный анекдот"));
        botCommands.add(new BotCommand(BotCommands.SPY.getCommand(), "Случайный анекдот про Штирлица или шпиона"));
        botCommands.add(new BotCommand(BotCommands.PETKA.getCommand(), "Случайный анекдот про Петьку и Василия Ивановича"));
        botCommands.add(new BotCommand(BotCommands.ANIMAL.getCommand(), "Случайный анекдот про лесных зверей"));
        try {
            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's commands: " + e.getMessage());
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            var message = update.getMessage();
            var chatId = message.getChatId();
            log.info("New message from User: {}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());

            BotCommands botCommand = caseHandlers.get(message.getText());
            if (botCommand == BotCommands.MORE) {
                botCommand = chatCommandsCache.getChatCurrentBotComm(chatId);
            }

            chatCommandsCache.setChatCurrentBotComm(chatId, botCommand);
            var replyMessage = botStateContext.processInputMessage(botCommand, message);
            sendMessage(chatId, replyMessage.getText());
        }
    }

    private void sendMessage(long chatId, String textSend) {

        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textSend);
        message.setReplyMarkup(getKeyboard());

        try {
            execute(message);

            log.info("New callbackQuery from ChatId: {}, with data: {}",
                    message.getChatId(), message.getText());

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    static private ReplyKeyboardMarkup getKeyboard() {
        var keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(BotCommands.MORE.getCommand());
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
