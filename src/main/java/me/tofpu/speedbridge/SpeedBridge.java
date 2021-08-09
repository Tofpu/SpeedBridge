package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandManager;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.config.Config;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.game.listener.functionality.BlockBreakListener;
import me.tofpu.speedbridge.game.listener.functionality.BlockPlaceListener;
import me.tofpu.speedbridge.game.listener.functionality.EntityDamageListener;
import me.tofpu.speedbridge.game.listener.functionality.FoodLevelChangeListener;
import me.tofpu.speedbridge.game.listener.machanic.PlayerInteractListener;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.data.listener.*;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

// TODO:
// Add a game package, game service & game controller! (DONE)
// Handle commands (DONE)
// Complete all the to-do's
// Create a queue system (refer to discord requester for more info)
// Listen to PlayerJoinEvent, PlayerLeaveEvent & PlayerPressurePlate or whatever, to load & de-load data, and for getting timed. (DONE)
// Save Islands & Users data (USE GSON) (DONE)
// Have the island & user controllers load data instead! (CANCELLED, USED DATAMANAGER & RESPECTIVE SERVICES INSTEAD)
public final class SpeedBridge extends JavaPlugin {
    private Game game;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Config.initialize(this);

        this.game = new Game(getDataFolder());
        final DataManager dataManager = getGame().getDataManager();

        dataManager.initialize();
        ModeManager.getModeManager().initialize();

        dataManager.load();

        final IUserService userService = getGame().getUserService();
        final IIslandService islandService = getGame().getIslandService();
        final IGameService gameService = getGame().getGameService();
        final ILobbyService lobbyService = getGame().getLobbyService();

        getCommand("speedbridge").setExecutor(new CommandManager(this.game.getGameController(), gameService, lobbyService));

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(lobbyService, dataManager), this);
        pluginManager.registerEvents(new PlayerQuitListener(userService, islandService, gameService, dataManager), this);
        pluginManager.registerEvents(new PlayerInteractListener(userService, islandService, gameService), this);
        pluginManager.registerEvents(new BlockPlaceListener(userService, islandService, gameService), this);
        pluginManager.registerEvents(new BlockBreakListener(userService, islandService, gameService), this);
        pluginManager.registerEvents(new EntityDamageListener(gameService), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(gameService), this);

        // /RELOAD BUG FIX
        Bukkit.getOnlinePlayers().forEach(player -> dataManager.loadUser(player.getUniqueId()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        final DataManager dataManager = this.game.getDataManager();
        dataManager.save();
    }

    public Game getGame() {
        return game;
    }
}
