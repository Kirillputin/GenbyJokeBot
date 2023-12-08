package ru.genby.jokebot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.genby.jokebot.TelegramJokeBot;

@Slf4j //LOG
@Component
public class BotInitializer {
    final TelegramJokeBot bot;

    public BotInitializer(TelegramJokeBot bot) {
        this.bot = bot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error occurred from User: {}, chatId: {}, with text: {}",
                    bot.getMe().getUserName(), bot.getMe().getId(), e.getMessage());
        }
    }
}
