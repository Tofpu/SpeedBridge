package me.tofpu.speedbridge.command.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.RegisteredCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Private;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

import java.util.Map;

public class BridgeBaseCommand extends BaseCommand {
    private final String identifier;

    public BridgeBaseCommand(String identifier) {
        this.identifier = identifier;
    }

    public void onHelp(final Player player) {
        player.sendMessage(Util.colorize("&e&l&m<&6&m------&r &e&lSpeedBridge Commands &6&m------&e&l&m>"));
        for (final Map.Entry<String, RegisteredCommand> test : getSubCommands().entries()) {
            final RegisteredCommand<?> command = test.getValue();
            if (command.isPrivate()) continue;
            player.sendMessage(Util.format(command));
        }
        player.sendMessage(Util.colorize("&e&l&m<&r&6&m----------------&e&l&m>&r &e&l&m<&r&6&m----------------&e&l&m>"));
    }

    public void onUnknownCommand(final Player player) {
        player.sendMessage(Util.colorize("&cUnknown command, type /" + identifier + " help for more info!"));
    }
}
