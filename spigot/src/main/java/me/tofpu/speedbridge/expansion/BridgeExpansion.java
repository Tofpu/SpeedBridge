package me.tofpu.speedbridge.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.lobby.Leaderboard;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BridgeExpansion extends PlaceholderExpansion {
    private final PluginDescriptionFile description;

    private final UserService userService;
    private final GameService gameService;
    private final LobbyService lobbyService;

    public BridgeExpansion(final PluginDescriptionFile description, final UserService userService, final GameService gameService, final LobbyService lobbyService) {
        this.description = description;
        this.userService = userService;
        this.gameService = gameService;
        this.lobbyService = lobbyService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "bridge";
    }

    @Override
    public @NotNull String getAuthor() {
        return description.getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return description.getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.isEmpty()) return "";
        final String[] args = params.split("_");
        final ExpansionType type = ExpansionType.getMatch(args[0]);
        if (type == null) return "";

        final User user = userService.searchForUUID(player.getUniqueId());
        boolean isNull = user == null;

        final Timer timer;
        switch (type) {
            case ISLAND:
                Integer slot = null;
                if (isNull || ((slot = user.properties().islandSlot()) == null)) return "Lobby";
                return slot + "";
            case LIVE_TIMER:
                if (isNull || (timer = gameService.getTimer(user)) == null) return "0";
                return Util.toSeconds(timer.start()) + "";
            case SCORE:
                if (isNull || (timer = user.properties().timer()) == null) return "N/A";
                return timer.result() + "";
            case LEADERBOARD:
                if (args.length <= 1) return null;
                final Leaderboard leaderboard = lobbyService.getLeaderboard();
                System.out.println(Arrays.toString(args));
                final Integer integer = Util.parseInt(args[1]);
                return integer == null ? "Provide a number!" : integer == 0 ? "Only number 1 or above is allowed!" : leaderboard.parse(integer - 1);
            default:
                return "";
        }
    }
}
