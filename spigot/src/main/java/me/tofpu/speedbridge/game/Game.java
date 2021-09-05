package me.tofpu.speedbridge.game;

import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.command.CommandHandler;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.listener.PlayerJoinListener;
import me.tofpu.speedbridge.data.listener.PlayerQuitListener;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.listener.functionality.BlockBreakListener;
import me.tofpu.speedbridge.game.listener.functionality.EntityDamageListener;
import me.tofpu.speedbridge.game.listener.functionality.FoodLevelChangeListener;
import me.tofpu.speedbridge.game.listener.machanic.BlockPlaceListener;
import me.tofpu.speedbridge.game.listener.machanic.PlayerInteractListener;
import me.tofpu.speedbridge.game.service.GameServiceImpl;
import me.tofpu.speedbridge.island.service.IslandServiceImpl;
import me.tofpu.speedbridge.lobby.service.LobbyServiceImpl;
import me.tofpu.speedbridge.user.service.UserServiceImpl;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Game {
    public static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1);

    private final SpeedBridge speedBridge;
    private final DataManager dataManager;

    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;

    private final GameController gameController;
    private final GameService gameService;

    private final List<Listener> listeners;

    public Game(final SpeedBridge speedBridge) {
        this.speedBridge = speedBridge;
        this.dataManager = new DataManager();

        this.islandService = new IslandServiceImpl(dataManager);
        this.userService = new UserServiceImpl(dataManager);
        this.lobbyService = new LobbyServiceImpl();

        this.gameController = new GameController(islandService);
        this.gameService = new GameServiceImpl(speedBridge, islandService, userService, lobbyService);

        this.listeners = Arrays.asList(
                new PlayerJoinListener(lobbyService, dataManager),
                new PlayerQuitListener(userService, gameService, dataManager),
                new PlayerInteractListener(userService, islandService, gameService),
                new BlockPlaceListener(userService, islandService, gameService),
                new BlockBreakListener(userService, islandService, gameService),
                new EntityDamageListener(gameService),
                new FoodLevelChangeListener(gameService)
        );
    }

    public void initialize(){
        // initializing the commands
        new CommandHandler(this, speedBridge);

        // starting the basic metrics
        new Metrics(speedBridge, 12679);

        // initializing the files
        this.dataManager.initialize(
                this.islandService,
                this.userService,
                this.lobbyService,
                this.speedBridge,
                this.speedBridge.getDataFolder()
        );

        registerListeners();
    }

    private void registerListeners(){
        final PluginManager manager = Bukkit.getPluginManager();
        for (final Listener listener : this.listeners) {
            manager.registerEvents(listener, this.speedBridge);
        }
        this.listeners.clear();
    }

    public IslandService islandService() {
        return this.islandService;
    }

    public UserService userService() {
        return this.userService;
    }

    public GameController gameController() {
        return this.gameController;
    }

    public GameService gameService() {
        return this.gameService;
    }

    public LobbyService lobbyService() {
        return this.lobbyService;
    }

    public DataManager dataManager() {
        return this.dataManager;
    }
}
