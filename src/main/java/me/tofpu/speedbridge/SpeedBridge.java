package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandManager;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.config.Config;
import me.tofpu.speedbridge.data.listener.PlayerJoinListener;
import me.tofpu.speedbridge.data.listener.PlayerQuitListener;
import me.tofpu.speedbridge.dependency.register.DependencyRegister;
import me.tofpu.speedbridge.expansion.BridgeExpansion;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.game.listener.functionality.BlockBreakListener;
import me.tofpu.speedbridge.game.listener.functionality.BlockPlaceListener;
import me.tofpu.speedbridge.game.listener.functionality.EntityDamageListener;
import me.tofpu.speedbridge.game.listener.functionality.FoodLevelChangeListener;
import me.tofpu.speedbridge.game.listener.machanic.PlayerInteractListener;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

// TODO:
// Add a game package, game service & game controller! (DONE)
// Handle commands (DONE)
// Complete all the to-do's
// Create a queue system (refer to discord requester for more info)
// Listen to PlayerJoinEvent, PlayerLeaveEvent & PlayerPressurePlate or whatever, to load & de-load data, and for getting timed. (DONE)
// Save Islands & Users data (USE GSON) (DONE)
// Have the island & user controllers load data instead! (CANCELLED, USED DATAMANAGER & RESPECTIVE SERVICES INSTEAD)
public final class SpeedBridge extends JavaPlugin {
    private final List<Listener> listeners;

    private final Game game;

    public SpeedBridge() {
        this.game = new Game(getDataFolder());

        final DataManager dataManager = getGame().getDataManager();
        final IUserService userService = getGame().getUserService();
        final IIslandService islandService = getGame().getIslandService();
        final IGameService gameService = getGame().getGameService();
        final ILobbyService lobbyService = getGame().getLobbyService();

        this.listeners = Arrays.asList(
                new PlayerJoinListener(lobbyService, dataManager),
                new PlayerQuitListener(userService, islandService, gameService, dataManager),
                new PlayerInteractListener(userService, islandService, gameService),
                new BlockPlaceListener(userService, islandService, gameService),
                new BlockBreakListener(userService, islandService, gameService),
                new EntityDamageListener(gameService),
                new FoodLevelChangeListener(gameService));
    }

    @Override
    public void onEnable() {
        final DataManager dataManager = getGame().getDataManager();
        Config.initialize(this);

        dataManager.initialize();
        ModeManager.getModeManager().initialize();

        DependencyRegister.loadAll(this);
        dataManager.load();

        registerPlaceholderApi();
        registerListeners();
        getCommand("speedbridge").setExecutor(new CommandManager(getGame().getGameController(), getGame().getGameService(), getGame().getLobbyService()));


        // RELOAD BUG FIX
        Bukkit.getOnlinePlayers().forEach(player -> dataManager.loadUser(player.getUniqueId()));
    }

    @Override
    public void onDisable() {
        final DataManager dataManager = this.game.getDataManager();
        dataManager.save();
    }

    public void registerPlaceholderApi() {
        if (DependencyRegister.get("PlaceholderAPI").getDependency() == null) return;
        getLogger().info("Hooked into PlaceholderAPI");
        new BridgeExpansion(getDescription(), getGame().getUserService()).register();
    }

    public void registerListeners() {
        final PluginManager manager = Bukkit.getPluginManager();
        for (final Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }
    }

    public Game getGame() {
        return game;
    }
}
