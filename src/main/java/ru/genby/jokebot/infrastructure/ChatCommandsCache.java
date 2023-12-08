package ru.genby.jokebot.infrastructure;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatCommandsCache {
    private Map<Long, BotCommands> usersBotComm = new HashMap<>();

    public void setChatCurrentBotComm(Long chatId, BotCommands botCommands) {
        usersBotComm.put(chatId, botCommands);
    }

public BotCommands getChatCurrentBotComm(Long chatId){
     BotCommands botCommand = usersBotComm.get(chatId);
        if (botCommand == null){
            botCommand = BotCommands.START;
        }
        return botCommand;
}
}
