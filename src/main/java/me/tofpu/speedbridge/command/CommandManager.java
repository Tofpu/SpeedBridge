package me.tofpu.speedbridge.command;

import me.tofpu.speedbridge.island.controller.IslandController;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.controller.UserController;
import me.tofpu.speedbridge.user.impl.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CommandManager implements CommandExecutor {
    private final IslandController islandController;
    private final UserController userController;

    public CommandManager(final IslandController islandController, final UserController userController) {
        this.islandController = islandController;
        this.userController = userController;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            return false;
        }
        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();

        if (args.length < 1) return false;
        switch (args[0]){
            case "join":
                final List<Island> availableIslands = islandController.getAvailableIslands();
                if (availableIslands.size() > 1){
                    final Island island = availableIslands.get(0);
                    if (!island.hasLocation()){
                        // TODO: DOESN'T HAVE A LOCATION, TRY AGAIN.
                        return false;
                    }
                    IUser user = userController.searchForUUID(uuid);
                    if (user == null){
                        user = new User(uuid);
                        userController.addUser(user);
                    }
                    island.setTakenBy(user);
                    player.teleport(island.getLocation());
                    // TODO: SEND MESSAGE THAT THEY'VE BEEN TELEPORTED
                    return false;
                }
                // TODO: NO ISLAND AVAILABLE
                break;
            case "leave":
                final IUser user = userController.searchForUUID(uuid);
                if (user == null){
                    // TODO: USER IS NOT IN A ISLAND
                    return false;
                }
                // TODO: TELEPORT PLAYER TO LOBBY!

                break;
        }
        return false;
    }
}
