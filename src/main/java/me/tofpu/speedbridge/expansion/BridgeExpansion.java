package me.tofpu.speedbridge.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tofpu.speedbridge.expansion.type.ExpansionType;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.properties.timer.Timer;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class BridgeExpansion extends PlaceholderExpansion {
    private final PluginDescriptionFile description;

    private final UserService userService;
    private final GameService gameService;

    public BridgeExpansion(final PluginDescriptionFile description, final UserService userService, final GameService gameService) {
        this.description = description;
        this.userService = userService;
        this.gameService = gameService;
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
        final ExpansionType type = ExpansionType.getMatch(params);
        if (type == null) return "";

        final User user = userService.searchForUUID(player.getUniqueId());
        boolean isNull = user == null;

        final Timer timer;
        switch (type) {
            case ISLAND:
                Integer slot = null;
                if (isNull || ((slot = user.getProperties().getIslandSlot()) == null)) return "Lobby";
                return slot + "";
            case LIVE_TIMER:
                if (isNull || (timer = gameService.getTimer(user)) == null) return "0";
                return Util.toSeconds(timer.getStart()) + "";
            case SCORE:
                if (isNull || (timer = user.getProperties().getTimer()) == null) return "N/A";
                return timer.getResult() + "";
            default:
                return "";
        }
    }
}
