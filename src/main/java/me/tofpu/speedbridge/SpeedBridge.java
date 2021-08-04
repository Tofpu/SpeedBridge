package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandManager;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.controller.IslandController;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.island.service.impl.IslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.controller.UserController;
import me.tofpu.speedbridge.user.impl.User;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.user.service.impl.UserService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

// TODO:
// Add a game package, game service & game controller!
public final class SpeedBridge extends JavaPlugin {
    private final IslandController islandController;
    private final UserController userController;

    public SpeedBridge(){
        IIslandService islandService = new IslandService();
        this.islandController = new IslandController(islandService);

        IUserService userService = new UserService();
        this.userController = new UserController(userService);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("speedbridge").setExecutor(new CommandManager(islandController, userController));

        // TES1
        final Island island = new Island(10);
        this.islandController.addIsland(island);

        final Island islandResult = this.islandController.getIslandBySlot(10);

        // USER TEST
        final UUID uuid = UUID.randomUUID();

        final IUser user = new User(uuid);
        this.userController.addUser(user);

        final IUser userResult = this.userController.searchForUUID(uuid);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
