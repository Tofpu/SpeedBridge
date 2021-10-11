package me.tofpu.speedbridge.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tofpu.speedbridge.api.model.service.GameService;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.service.IslandService;
import me.tofpu.speedbridge.api.model.object.leaderboard.Leaderboard;
import me.tofpu.speedbridge.api.model.service.LeaderboardService;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.service.UserService;
import me.tofpu.speedbridge.api.model.object.user.timer.Timer;
import me.tofpu.speedbridge.api.model.object.leaderboard.LeaderboardType;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class BridgeExpansion extends PlaceholderExpansion {
    private final PluginDescriptionFile description;

    private final UserService userService;
    private final IslandService islandService;
    private final GameService gameService;
    private final LeaderboardService leaderboardService;

    public BridgeExpansion(final PluginDescriptionFile description, final UserService userService, final IslandService islandService, final GameService gameService, final LeaderboardService leaderboardService) {
        this.description = description;
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
        this.leaderboardService = leaderboardService;
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
        final ExpansionType type = ExpansionType.match(args[0]);
        if (type == null) return "";

        final User user = userService.find(player.getUniqueId());
        boolean isNull = user == null;

        final Timer timer;
        final Integer slot;
        switch (type) {
            case SCORE:
                if (isNull || (timer = user.properties().timer()) == null) {
                    return "N/A";
                }
                return timer.result() + "";
            case BLOCKS:
                if (isNull || ((slot = user.properties().islandSlot()) == null)) {
                    return "0";
                }
                final Island island = islandService.getIslandBySlot(slot);

                if (island == null) {
                    return "0";
                } else {
                    return island.placedBlocks().size() + "";
                }
            case ISLAND:
                if (isNull || ((slot = user.properties().islandSlot()) == null)) {
                    return "Lobby";
                }
                return slot + "";
            case LIVE_TIMER:
                if (isNull || (timer = gameService.getTimer(user)) == null) {
                    return "0";
                }
                return Util.toSeconds(timer.start()) + "";
            case LEADERBOARD:
                if (args.length <= 1) return null;
                final LeaderboardType leaderboardType = LeaderboardType.match(args[1]);
                final Leaderboard leaderboard = leaderboardService.get(leaderboardType == null ? LeaderboardType.GLOBAL : leaderboardType);

                final Integer integer = Util.parseInt(leaderboardType == null ? args[1] : args[2]);
                return integer == null ? "Provide a number!" : integer == 0 ? "Only number 1 or above is allowed!" : leaderboard
                        .parse(integer - 1);
            default:
                return "";
        }
    }
}
