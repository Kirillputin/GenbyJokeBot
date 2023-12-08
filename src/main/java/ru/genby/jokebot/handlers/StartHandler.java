package ru.genby.jokebot.handlers;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.jokebot.infrastructure.BotCommands;
import ru.genby.jokebot.infrastructure.InputMessageHandler;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class StartHandler implements InputMessageHandler {
    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotCommands getHandlerName() {
        return BotCommands.START;
    }

    private SendMessage processUsersInput(Message inputMsg) {
      return new SendMessage(String.valueOf(inputMsg.getChatId()), "Привет! Я бот по подбору анекдотов. \nЧтобы выбрать тему воспользуйтесь 'Меню'.");
    }
}
