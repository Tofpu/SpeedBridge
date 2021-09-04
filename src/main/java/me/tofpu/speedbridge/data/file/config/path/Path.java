package me.tofpu.speedbridge.data.file.config.path;

import me.tofpu.speedbridge.data.file.config.output.type.OutputType;

public enum Path {
    SETTINGS_MAX_SLOTS("settings.max-slots"),

    MESSAGES_JOINED("messages.joined"),
    MESSAGES_LEFT("messages.left"),
    MESSAGES_NO_AVAILABLE("messages.no-available", "&cThere is no available island right now, try again later!"),
    MESSAGES_NOT_AVAILABLE("messages.not-available"),
    MESSAGES_NOT_PLAYING("messages.not-playing"),
    MESSAGES_ALREADY_JOINED("messages.already-joined"),
    MESSAGES_YOUR_SCORE("messages.your-score"),

    MESSAGES_CANNOT_EDIT("messages.cannot-edit"),
    MESSAGES_INSERT_NUMBER("messages.insert-number"),
    MESSAGES_NO_LOBBY("messages.no-lobby"),
    MESSAGES_INVALID_TYPE("messages.invalid-type"),
    MESSAGES_SINGLE_EDIT_ONLY("messages.single-edit-only"),

    MESSAGES_RELOADED("messages.reloaded"),
    MESSAGES_ISLAND_CREATION("messages.island-creation"),
    MESSAGES_ISLAND_MODIFICATION("messages.island-modification"),
    MESSAGES_LOBBY_LOCATION("messages.lobby-location"),
    MESSAGES_ISLAND_EXISTS("messages.island-exists"),
    MESSAGES_INVALID_ISLAND("messages.invalid-island"),
    MESSAGES_ISLAND_COMPLETED("messages.island-completed"),
    MESSAGES_ISLAND_INCOMPLETE("messages.island-incomplete"),

    MESSAGES_GUIDE("messages.guide", OutputType.STRING_LIST),

    MESSAGES_NOT_BEATEN("messages.not-beaten"),
    MESSAGES_BEATEN_SCORE("messages.beaten-score"),
    MESSAGES_SCORED("messages.scored"),

    MESSAGES_CANCEL_SETUP("messages.cancel-setup"),
    MESSAGES_NO_SETUP("messages.no-setup"),
    MESSAGES_NO_COMPLETE("messages.no-complete");
    private final String path;
    private OutputType type;
    private String defaultMessage;

    Path(String path) {
        this(path, OutputType.STRING);
    }

    Path(String path, String defaultMessage) {
        this.path = path;
        this.type = OutputType.STRING;
        this.defaultMessage = defaultMessage;
    }

    Path(String path, OutputType type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public OutputType getType() {
        return type;
    }
}
