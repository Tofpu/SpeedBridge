package me.tofpu.speedbridge.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class BridgeExpansion extends PlaceholderExpansion {
    private final PluginDescriptionFile description;
    private final IUserService userService;

    public BridgeExpansion(final PluginDescriptionFile description, final IUserService userService) {
        this.description = description;
        this.userService = userService;
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

        switch (args[1]){
            case "record":
                final IUser user = userService.searchForUUID(player.getUniqueId());
                if (user == null) return "0";
                return user.getProperties().getTimer().getResult() + "";
            default:
                return "";
        }
    }
}
