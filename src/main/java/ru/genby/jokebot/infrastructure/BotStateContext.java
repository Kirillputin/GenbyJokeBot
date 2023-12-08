package ru.genby.jokebot.infrastructure;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {
    private Map<BotCommands, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotCommands currentCommand, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentCommand);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotCommands currentCommand) {
        if (BotCommands.MORE == currentCommand) {
            return messageHandlers.get(BotCommands.MORE);
        }
        if (isProcess(currentCommand)) {
            return messageHandlers.get(BotCommands.PROCESS);
        }

        return messageHandlers.get(currentCommand);
    }

    private boolean isProcess(BotCommands currentCommand) {
        switch (currentCommand) {
            case SPY:
            case ANIMAL:
            case PETKA:
                return true;
            default:
                return false;
        }
    }
}
