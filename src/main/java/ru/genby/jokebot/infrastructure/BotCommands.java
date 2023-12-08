package ru.genby.jokebot.infrastructure;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BotCommands {
    START("/start"),
    PROCESS,
    RANDOM("/random"),
    SPY("/spy"),
    PETKA("/petka"),
    ANIMAL("/animal"),
    MORE("Еще!");

    private String command;

    BotCommands() {

    }

    public String getCommand() {
        return command;
    }
}
