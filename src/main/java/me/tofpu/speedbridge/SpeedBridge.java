package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandManager;
import me.tofpu.speedbridge.config.CachedConfig;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.listener.PlayerInteractListener;
import me.tofpu.speedbridge.listener.PlayerJoinListener;
import me.tofpu.speedbridge.listener.PlayerQuitListener;
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
        saveDefaultConfig();

        CachedConfig.initialize(this.getConfig());

        this.game = new Game(getDataFolder());
        final DataManager dataManager = this.game.getDataManager();
        dataManager.initialize();
        dataManager.loadIslands();

        getCommand("speedbridge").setExecutor(new CommandManager(this.game.getGameController(), this.game.getGameService()));

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(dataManager), this);
        pluginManager.registerEvents(new PlayerQuitListener(dataManager), this);
        pluginManager.registerEvents(new PlayerInteractListener(this.game.getUserService(), this.game.getIslandService(), this.game.getGameService()), this);

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
