package me.tofpu.speedbridge.game;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.identifier.ConfigIdentifier;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.github.requestpluginsforfree.dependency.impl.PlaceholderDependency;
import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.api.SpeedBridgeAPI;
import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.leaderboard.LeaderboardService;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.command.CommandHandler;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.data.listener.PlayerJoinListener;
import me.tofpu.speedbridge.data.listener.PlayerQuitListener;
import me.tofpu.speedbridge.expansion.BridgeExpansion;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;
import me.tofpu.speedbridge.game.leaderboard.LeaderboardServiceImpl;
import me.tofpu.speedbridge.game.listener.functionality.BlockBreakListener;
import me.tofpu.speedbridge.game.listener.functionality.EntityDamageListener;
import me.tofpu.speedbridge.game.listener.functionality.FoodLevelChangeListener;
import me.tofpu.speedbridge.game.listener.machanic.BlockPlaceListener;
import me.tofpu.speedbridge.game.listener.machanic.PlayerInteractListener;
import me.tofpu.speedbridge.game.service.GameServiceImpl;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.island.service.IslandServiceImpl;
import me.tofpu.speedbridge.lobby.LobbyServiceImpl;
import me.tofpu.speedbridge.user.service.UserServiceImpl;
import me.tofpu.speedbridge.util.UpdateChecker;
import me.tofpu.speedbridge.util.Util;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class Game {
    public static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(1);

    private final SpeedBridge speedBridge;
    private final DataManager dataManager;

    private final IslandServiceImpl islandService;
    private final UserServiceImpl userService;
    private final LobbyServiceImpl lobbyService;

    private final LeaderboardServiceImpl leaderboardService;

    private final GameController gameController;
    private final GameService gameService;

    private final List<Listener> listeners;

    public Game(final SpeedBridge speedBridge) {
        this.speedBridge = speedBridge;
        this.dataManager = new DataManager(speedBridge);

        this.islandService = new IslandServiceImpl();
        this.userService = new UserServiceImpl();
        this.lobbyService = new LobbyServiceImpl();

        this.leaderboardService = new LeaderboardServiceImpl();

        this.gameController = new GameController(islandService);
        this.gameService = new GameServiceImpl(speedBridge, islandService, userService, lobbyService, leaderboardService);

        this.listeners = new ArrayList<>(Arrays.asList(
                new PlayerJoinListener(userService, lobbyService, leaderboardService),
                new PlayerQuitListener(userService, gameService, leaderboardService, dataManager),
                new PlayerInteractListener(userService, islandService, gameService),
                new BlockPlaceListener(userService, islandService, gameService),
                new BlockBreakListener(userService, islandService, gameService),
                new EntityDamageListener(gameService),
                new FoodLevelChangeListener(gameService)
        ));
    }

    private void initializePlaceholderApi() {
        // registering the placeholder dependency
        DependencyAPI.register(new PlaceholderDependency());

        // initializing the dependency api
        DependencyAPI.initialize(speedBridge);

        // if it's not available, return
        if (!DependencyAPI.get("PlaceholderAPI").isAvailable()) return;

        Util.isPlaceholderHooked = true;
        new BridgeExpansion(speedBridge.getDescription(), userService, islandService, gameService, leaderboardService).register();
    }

    private void registerListeners(){
        final PluginManager manager = Bukkit.getPluginManager();
        for (final Listener listener : this.listeners) {
            manager.registerEvents(listener, this.speedBridge);
        }
        this.listeners.clear();
    }

    public void initialize() {
        // initializing phase

        // initializing the API
        SpeedBridgeAPI.setInstance(this.speedBridge.getLogger(), this.userService, this.islandService, this.gameService, this.lobbyService);

        // initializing the files
        this.dataManager.initialize(this.islandService, this.userService, this.lobbyService, this.leaderboardService, this.speedBridge, this.speedBridge.getDataFolder());

        this.userService.initialize(this.dataManager);
        this.islandService.initialize(this.dataManager);


        // update checker async
        UpdateChecker.init(speedBridge, 95918).requestUpdateCheck().whenComplete((updateResult, throwable) -> {
            final Logger logger = this.speedBridge.getLogger();

            if (updateResult.getReason() == UpdateChecker.UpdateReason.NEW_UPDATE) {
                logger.warning("You're not on the latest version of SpeedBridge!");
                logger.warning("It's highly recommended to download the latest version at https://www.spigotmc.org/resources/speedbridge-1-free-bridge-trainer-rpf.95918/!");
            } else if (updateResult.getReason() == UpdateChecker.UpdateReason.UP_TO_DATE) {
                speedBridge.getLogger().warning("You're using the latest version of SpeedBridge!");
            }
        });

        final ConfigIdentifier settingsIdentifier = ConfigIdentifier.of("settings", dataManager.getPluginFiles()[0].configuration());
        final ConfigIdentifier messagesIdentifier = ConfigIdentifier.of("messages", dataManager.getPluginFiles()[1].configuration());

        // initializing the two configs
        ConfigAPI.initialize(settingsIdentifier, messagesIdentifier);

        // reloading the configurations
        dataManager.reload();

        // initializing the leaderboards
        leaderboardService.initialize(this.dataManager.getFiles()[3]);

        // initializing the mode manager
        ModeManager.getModeManager().initialize();

        initializePlaceholderApi();
    }

    public void load() {
        // loading phase

        // initializing the commands
        new CommandHandler(this, speedBridge);

        // starting the basic metrics
        new Metrics(speedBridge, 12679);

        registerListeners();

        try {
            this.dataManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // adds in options that are missing from the configurations
        boolean update = false;
        for (Path.Value<?> value : Path.values()) {
            final String identifier = value.getPathType().name();

            final FileConfiguration configuration = ConfigAPI.get(identifier).configuration();
            final String path = value.getPath();

            if (configuration.get(path, null) != null) continue;
            update = true;

            configuration.set(path, value.getDefaultValue());
        }

        // if there was a change, save & reload
        if (update) {
            dataManager.save();
            dataManager.reload();
        }

        // reload patch
        Bukkit.getOnlinePlayers().forEach(player -> dataManager.loadUser(player.getUniqueId()));

        // starting up the leaderboard
        leaderboardService.compute(null, leaderboard -> ((AbstractLeaderboard) leaderboard).start(speedBridge));
    }

    public IslandServiceImpl islandService() {
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

    public LeaderboardService leaderboardManager() {
        return leaderboardService;
    }

    public LobbyService lobbyService() {
        return this.lobbyService;
    }

    public DataManager dataManager() {
        return this.dataManager;
    }
}
