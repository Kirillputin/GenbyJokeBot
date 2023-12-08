package ru.genby.jokebot.handlers;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.genby.jokebot.infrastructure.BotCommands;
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
public class RandomHandler implements InputMessageHandler {
    private JokeRepository JokeRepository;
    private ExceptionRepository exceptionJoke;

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotCommands getHandlerName() {
        return BotCommands.RANDOM;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        var chatId = inputMsg.getChatId();
        List<Joke> jokes;
        List<ExceptionJoke> exceptionList = exceptionJoke.getExceptionJokesByChatid(chatId);

        if (exceptionList.isEmpty()) {
            jokes = JokeRepository.findAll();

        } else {
            List<Long> exceptionNumberJoke = exceptionList.stream()
                    .map(ExceptionJoke::getNumberJoke)
                    .collect(Collectors.toList());

            jokes = JokeRepository.getJokeByIdNotIn(exceptionNumberJoke);
        }

        if (jokes.isEmpty()) {
            return new SendMessage(String.valueOf(chatId), "Новых анекдотов нет");
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
