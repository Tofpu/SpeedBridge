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
    public static final Value<String> SETTINGS_BLOCK = new Value<>(SETTINGS + "block", "WOOL", PathType.SETTINGS);

    private static final String LEADERBOARD = SETTINGS + "leaderboard.";
    public static final Value<Integer> LEADERBOARD_SIZE = new Value<>(LEADERBOARD + "size", 10, PathType.SETTINGS);
    public static final Value<String> LEADERBOARD_HEADER = new Value<>(LEADERBOARD + "header", "&eLeaderboard", PathType.SETTINGS);
    public static final Value<String> LEADERBOARD_STYLE = new Value<>(LEADERBOARD + "style", "&e{position}. {name} &a({score})", PathType.SETTINGS);

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

    public static final Value<String> MESSAGES_ISLAND_REMOVAL = new Value<>(MESSAGES + "island-removal", "&eIsland &6%slot% &ehas been removed!\n&eYou can revert the removal by typing /island revert", PathType.MESSAGES);
    public static final Value<String> messages_ISLAND_REMOVAL_FAIL = new Value<>(MESSAGES + "island-removal-fail", "&eIsland &6%slot% &ecouldn't be removed... check the console!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_REVERT = new Value<>(MESSAGES + "island-revert", "&eIsland &6%slot% &ehas been reverted back", PathType.MESSAGES);
    public static final Value<String> MESSAGES_ISLAND_REVERT_FAIL = new Value<>(MESSAGES + "island-revert-fail", "&cThere is nothing to revert back!", PathType.MESSAGES);

    public static final Value<String> MESSAGES_CANCEL_SETUP = new Value<>(MESSAGES + "cancel-setup", "&cThe current island setup has been canceled!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_SETUP = new Value<>(MESSAGES + "no-setup", "&cYou don't have anything to cancel.", PathType.MESSAGES);

    public static final Value<String> MESSAGES_NOT_BEATEN = new Value<>(MESSAGES + "not-beaten", "&cYou didn't beat your personal best score. (%score% seconds)", PathType.MESSAGES);
    public static final Value<String> MESSAGES_BEATEN_SCORE = new Value<>(MESSAGES + "beaten-score", "&aYou topped your old personal best score by %calu_score% seconds!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SCORED = new Value<>(MESSAGES + "scored", "&eYou scored &a%scored% &eseconds!", PathType.MESSAGES);

    public static final Value<String> MESSAGES_SPECTATING = new Value<>(MESSAGES + "spectating", "&eYou are spectating &6%player%&e!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOTIFY_SPECTATING = new Value<>(MESSAGES + "notify-spectating", "&6%player% &eis spectating!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NO_LONGER_SPECTATING = new Value<>(MESSAGES + "no-longer-spectating", "&eYou're no longer spectating &6%player%!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_NOTIFY_NOT_SPECTATING = new Value<>(MESSAGES + "notify-not-spectating", "&6%player% &eis no longer spectating!", PathType.MESSAGES);
    
    public static final Value<String> MESSAGES_SPECTATOR_BEATEN_SCORE = new Value<>(MESSAGES + "spectator-beaten-score", "&6%player% &etopped their personal score by &a%calu_score% &eseconds!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SPECTATOR_SCORED = new Value<>(MESSAGES + "spectator-scored", "&6%player% &escored &a%scored% &eseconds!", PathType.MESSAGES);

    public static final Value<String> MESSAGES_SPECTATOR_UNKNOWN = new Value<>(MESSAGES + "spectate-unknown", "&eYou cannot spectate offline players!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SPECTATOR_SELF = new Value<>(MESSAGES + "spectate-self", "&eYou cannot spectate yourself!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SPECTATOR_SELF_PLAYING = new Value<>(MESSAGES + "spectate-self-playing", "&eYou cannot spectate while playing!", PathType.MESSAGES);
    public static final Value<String> MESSAGES_SPECTATOR_TARGET = new Value<>(MESSAGES + "spectate-target", "&eYou cannot spectate somebody who isn't playing!", PathType.MESSAGES);

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