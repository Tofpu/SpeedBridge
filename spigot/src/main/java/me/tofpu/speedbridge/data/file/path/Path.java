package me.tofpu.speedbridge.data.file.path;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.config.ConfigType;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private static final List<Value<?>> VALUE_LIST = new ArrayList<>();

    private static final String SETTINGS = "settings.";
    private static final String MESSAGES = "messages.";

    public static final Value<Integer> SETTINGS_MAX_SLOTS = new Value<>(SETTINGS + ".max-slots", ConfigType.INTEGER, PathType.SETTINGS);

    public static final Value<String> MESSAGES_JOINED = new Value<>(MESSAGES + "joined", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_LEFT = new Value<>(MESSAGES + "left", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_AVAILABLE = new Value<String>(MESSAGES + "no-available", "&cThere is no available island right now; try again later!", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOT_AVAILABLE = new Value<>(MESSAGES + "not-available", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOT_PLAYING = new Value<>(MESSAGES + "not-playing", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_ALREADY_JOINED = new Value<>(MESSAGES + "already-joined", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_YOUR_SCORE = new Value<>(MESSAGES + "your-score", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> MESSAGES_CANNOT_EDIT = new Value<>(MESSAGES + "cannot-edit", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_INSERT_NUMBER = new Value<>(MESSAGES + "insert-number", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_LOBBY = new Value<>(MESSAGES + "no-lobby", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_INVALID_TYPE = new Value<>(MESSAGES + "invalid-type", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_SINGLE_EDIT_ONLY = new Value<>(MESSAGES + "single-edit-only", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> MESSAGES_RELOADED = new Value<>(MESSAGES + "reloaded", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_CREATION = new Value<>(MESSAGES + "island-creation", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_MODIFICATION = new Value<>(MESSAGES + "island-modification", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_LOBBY_LOCATION = new Value<>(MESSAGES + "lobby-location", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_EXISTS = new Value<>(MESSAGES + "island-exists", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_INVALID_ISLAND = new Value<>(MESSAGES + "invalid-island", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_COMPLETED = new Value<>(MESSAGES + "island-completed", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_INCOMPLETE = new Value<>(MESSAGES + "island-incomplete", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> MESSAGES_GUIDE = new Value<>(MESSAGES + "guide", ConfigType.STRING_LIST, PathType.MESSAGES);

    public static final Value<String> MESSAGES_NOT_BEATEN = new Value<>(MESSAGES + "not-beaten", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_BEATEN_SCORE = new Value<>(MESSAGES + "beaten-score", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_SCORED = new Value<>(MESSAGES + "scored", ConfigType.STRING, PathType.MESSAGES);

    public static final Value<String> MESSAGES_CANCEL_SETUP = new Value<>(MESSAGES + "cancel-setup", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_SETUP = new Value<>(MESSAGES + "no-setup", ConfigType.STRING, PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_COMPLETE = new Value<>(MESSAGES + "no-complete", ConfigType.STRING, PathType.MESSAGES);

    public static List<Value<?>> values() {
        return VALUE_LIST;
    }

    public static class Value<T> {
        private final String path;
        private final ConfigType<?> type;
        private final PathType pathType;

        private String defaultMessage;
        private T value;

        public Value(final String path, String defaultMessage, ConfigType<?> type, final PathType pathType) {
            this(path, type, pathType);
            this.defaultMessage = defaultMessage;
        }

        public Value(final String path, ConfigType<?> type, final PathType pathType) {
            this.path = path;
            this.type = type;
            this.pathType = pathType;
            this.defaultMessage = "";

            Path.VALUE_LIST.add(this);
            reload();
        }

        public T getValue() {
            return value;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public T reload() {
            this.value = ConfigAPI.get(pathType.name(), path, type);
            return value;
        }
    }
}