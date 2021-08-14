package me.tofpu.speedbridge.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.timer.Timer;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class BridgeExpansion extends PlaceholderExpansion {
    private final PluginDescriptionFile description;

    private final IUserService userService;
    private final IGameService gameService;

    public BridgeExpansion(final PluginDescriptionFile description, final IUserService userService, final IGameService gameService) {
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
        final String[] args = params.split("_");
        if (args.length < 1) return "";

        final IUser user = userService.searchForUUID(player.getUniqueId());
        switch (args[0]) {
            case "score":
                if (user == null) return "0";
                return user.getProperties().getTimer().getResult() + "";
            case "island":
                Integer slot = null;
                if (user == null || ((slot = user.getProperties().getIslandSlot()) == null)) return "Lobby";
                return slot + "";
            case "live-timer":
                final Timer timer;
                if (user == null || (timer = gameService.getTimer(user)) == null) return "N/A";
                return Util.toSeconds(timer.getStart()) + "";
            default:
                return "";
        }
    }
}
