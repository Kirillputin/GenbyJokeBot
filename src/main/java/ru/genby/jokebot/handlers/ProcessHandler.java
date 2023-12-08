package ru.genby.jokebot.handlers;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.jokebot.infrastructure.BotCommands;
import ru.genby.jokebot.infrastructure.ChatCommandsCache;
import ru.genby.jokebot.infrastructure.InputMessageHandler;
import ru.genby.jokebot.model.ExceptionJoke;
import ru.genby.jokebot.model.Joke;
import ru.genby.jokebot.repository.ExceptionRepository;
import ru.genby.jokebot.repository.JokeRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProcessHandler implements InputMessageHandler {
    private JokeRepository jokeRepository;
    private ExceptionRepository exceptionJoke;
    private ChatCommandsCache chatCommandsCache;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotCommands getHandlerName() {
        return BotCommands.PROCESS;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        var chatId = inputMsg.getChatId();
        var botCommand = chatCommandsCache.getChatCurrentBotComm(chatId);

        List<Joke> jokes;
        List<ExceptionJoke> exceptionList = exceptionJoke.getExceptionJokesByChatid(chatId);

        if (exceptionList.isEmpty()) {
            jokes = jokeRepository.getJokesByCategory(botCommand.toString());
        } else {
            List<Long> exceptionNumberJoke = exceptionList.stream()
                    .map(ExceptionJoke::getNumberJoke)
                    .collect(Collectors.toList());

            jokes = jokeRepository.getJokesByCategoryAndIdNotIn(String.valueOf(botCommand), exceptionNumberJoke);
        }

        if (jokes.isEmpty()) {
            return new SendMessage(String.valueOf(chatId), "Вы прочитали все анекдоты из выбранной вами рублики");
        }

        var joke = jokes.get(getRandomNumber(jokes));
        exceptionJoke.save(new ExceptionJoke(chatId, joke.getId()));

        return new SendMessage(String.valueOf(chatId), joke.getJoke());
    }

    private int getRandomNumber(List<Joke> jokeList) {
        var random = new Random();
        return random.nextInt(jokeList.size());
    }
}
