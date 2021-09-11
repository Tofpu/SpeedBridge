package me.tofpu.speedbridge.data.file.path;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.config.ConfigType;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private static final List<Value<?>> VALUE_LIST = new ArrayList<>();

    private static final String SETTINGS = "settings.";
    private static final String MESSAGES = "messages.";

    public static final Value<Boolean> SETTINGS_TELEPORT = new Value<>(SETTINGS + "teleport", true, PathType.SETTINGS);

    public static final Value<String> MESSAGES_JOINED = new Value<>(MESSAGES + "joined", PathType.MESSAGES);
    public static final Value<String> MESSAGES_LEFT = new Value<>(MESSAGES + "left", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_AVAILABLE = new Value<>(MESSAGES + "no-available", "&cThere is no available island right now; try again later!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOT_AVAILABLE = new Value<>(MESSAGES + "not-available", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOT_PLAYING = new Value<>(MESSAGES + "not-playing", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ALREADY_JOINED = new Value<>(MESSAGES + "already-joined", PathType.MESSAGES);
    public static final Value<String> MESSAGES_YOUR_SCORE = new Value<>(MESSAGES + "your-score", PathType.MESSAGES);

    public static final Value<String> MESSAGES_CANNOT_EDIT = new Value<>(MESSAGES + "cannot-edit", PathType.MESSAGES);
    public static final Value<String> MESSAGES_INSERT_NUMBER = new Value<>(MESSAGES + "insert-number", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_LOBBY = new Value<>(MESSAGES + "no-lobby", PathType.MESSAGES);
    public static final Value<String> MESSAGES_INVALID_TYPE = new Value<>(MESSAGES + "invalid-type", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SINGLE_EDIT_ONLY = new Value<>(MESSAGES + "single-edit-only", PathType.MESSAGES);

    public static final Value<String> MESSAGES_RELOADED = new Value<>(MESSAGES + "reloaded", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_CREATION = new Value<>(MESSAGES + "island-creation", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_MODIFICATION = new Value<>(MESSAGES + "island-modification", PathType.MESSAGES);
    public static final Value<String> MESSAGES_LOBBY_LOCATION = new Value<>(MESSAGES + "lobby-location", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_EXISTS = new Value<>(MESSAGES + "island-exists", PathType.MESSAGES);
    public static final Value<String> MESSAGES_INVALID_ISLAND = new Value<>(MESSAGES + "invalid-island", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_COMPLETED = new Value<>(MESSAGES + "island-completed", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_INCOMPLETE = new Value<>(MESSAGES + "island-incomplete", PathType.MESSAGES);

    public static final Value<String> MESSAGES_GUIDE = new Value<>(MESSAGES + "guide", PathType.MESSAGES);

    public static final Value<String> MESSAGES_NOT_BEATEN = new Value<>(MESSAGES + "not-beaten", PathType.MESSAGES);
    public static final Value<String> MESSAGES_BEATEN_SCORE = new Value<>(MESSAGES + "beaten-score", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SCORED = new Value<>(MESSAGES + "scored", PathType.MESSAGES);

    public static final Value<String> MESSAGES_CANCEL_SETUP = new Value<>(MESSAGES + "cancel-setup", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_SETUP = new Value<>(MESSAGES + "no-setup", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_COMPLETE = new Value<>(MESSAGES + "no-complete", PathType.MESSAGES);

    public static List<Value<?>> values() {
        return VALUE_LIST;
    }

    public static class Value<T> {
        private final String path;
        private final PathType pathType;

        private T value;
        private T defaultValue;

        public Value(final String path, T defaultValue, final PathType pathType) {
            this(path, pathType);
            this.defaultValue = defaultValue;
        }

        public Value(final String path, final PathType pathType) {
            this.path = path;
            this.pathType = pathType;

            reload();
            Path.VALUE_LIST.add(this);
        }

        public T getValue() {
            return value;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        public void reload() {
            this.value = ConfigAPI.get(pathType.name(), defaultValue, path);
        }
    }
}