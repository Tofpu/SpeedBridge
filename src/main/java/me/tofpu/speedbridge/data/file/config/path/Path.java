package me.tofpu.speedbridge.data.file.config.path;

public enum Path {
    SETTINGS_MAX_SLOTS("settings.max-slots"),

    MESSAGES_JOINED("messages.joined"),
    MESSAGES_LEFT("messages.left"),
    MESSAGES_NOT_AVAILABLE("messages.not-available"),
    MESSAGES_NOT_PLAYING("messages.not-playing"),
    MESSAGES_ALREADY_JOINED("messages.already-joined"),
    MESSAGES_YOUR_SCORE("messages.your-score"),
    MESSAGES_CANNOT_EDIT("messages.cannot-edit"),

    MESSAGES_INSERT_NUMBER("messages.insert-number"),
    MESSAGES_NO_LOBBY("messages.no-lobby"),

    MESSAGES_ISLAND_CREATION("messages.island-creation"),
    MESSAGES_LOBBY_LOCATION("messages.lobby-location"),
    MESSAGES_ISLAND_EXISTS("messages.island-exists"),
    MESSAGES_INVALID_ISLAND("messages.invalid-island"),
    MESSAGES_ISLAND_COMPLETED("messages.island-completed"),
    MESSAGES_ISLAND_INCOMPLETE("messages.island-incomplete"),

    MESSAGES_NOT_BEATEN("messages.not-beaten"),
    MESSAGES_BEATEN_SCORE("messages.beaten-score"),
    MESSAGES_SCORED("messages.scored");
    private final String path;

    Path(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
