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

    public static final Value<String> MESSAGES_JOINED = new Value<>(MESSAGES + "joined", "&aGood Luck!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_LEFT = new Value<>(MESSAGES + "left", "", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_AVAILABLE = new Value<>(MESSAGES + "no-available", "&cThere is no available island right now; try again later!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOT_AVAILABLE = new Value<>(MESSAGES + "not-available", "&cThis island is not available right now, try again later!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOT_PLAYING = new Value<>(MESSAGES + "not-playing", "&cYou're not playing!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ALREADY_JOINED = new Value<>(MESSAGES + "already-joined", "&cYou're already joined!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_YOUR_SCORE = new Value<>(MESSAGES + "your-score", "&eYour personal best score is: &a%score%&e!", PathType.MESSAGES);

    public static final Value<String> MESSAGES_CANNOT_EDIT = new Value<>(MESSAGES + "cannot-edit", "&cYou cannot edit an island while playing.", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_LOBBY = new Value<>(MESSAGES + "no-lobby", "&cYou have to setup a lobby first!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_INVALID_TYPE = new Value<>(MESSAGES + "invalid-type", "&cInvalid location type!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SINGLE_EDIT_ONLY = new Value<>(MESSAGES + "single-edit-only", "&cYou can edit an island one at a time.", PathType.MESSAGES);

    public static final Value<String> MESSAGES_RELOADED = new Value<>(MESSAGES + "reloaded", "&eSpeedBridge has applied the new configuration changes!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_CREATION = new Value<>(MESSAGES + "island-creation", "&aThe island has been created!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_MODIFICATION = new Value<>(MESSAGES + "island-modification", "&aYou can start modifying your island locations now!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_LOBBY_LOCATION = new Value<>(MESSAGES + "lobby-location", "&aSuccess!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_EXISTS = new Value<>(MESSAGES + "island-exists", "&cThat island already exists!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_INVALID_ISLAND = new Value<>(MESSAGES + "invalid-island", "&cThat island does not exist!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_COMPLETED = new Value<>(MESSAGES + "island-completed", "&eThe island setup has been completed!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_INCOMPLETE = new Value<>(MESSAGES + "island-incomplete", "&cIncomplete island setup, please double check!", PathType.MESSAGES);
    
    public static final Value<String> MESSAGES_CANCEL_SETUP = new Value<>(MESSAGES + "cancel-setup", "&cThe current island setup has been canceled!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_SETUP = new Value<>(MESSAGES + "no-setup", "&cYou don't have anything to cancel.", PathType.MESSAGES);

    public static final Value<String> MESSAGES_NOT_BEATEN = new Value<>(MESSAGES + "not-beaten", "&cYou didn't beat your personal best score. (%score% seconds)", PathType.MESSAGES);
    public static final Value<String> MESSAGES_BEATEN_SCORE = new Value<>(MESSAGES + "beaten-score", "&aYou topped your old personal best score by %calu_score% seconds!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SCORED = new Value<>(MESSAGES + "scored", "&eYou scored &a%scored% &eseconds!", PathType.MESSAGES);

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

        public String getPath() {
            return path;
        }

        public PathType getPathType() {
            return pathType;
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