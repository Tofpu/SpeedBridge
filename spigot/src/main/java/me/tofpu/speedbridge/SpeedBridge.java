package me.tofpu.speedbridge;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.identifier.ConfigIdentifier;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.github.requestpluginsforfree.dependency.impl.PlaceholderDependency;
import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.listener.PlayerJoinListener;
import me.tofpu.speedbridge.data.listener.PlayerQuitListener;
import me.tofpu.speedbridge.expansion.BridgeExpansion;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.game.listener.functionality.BlockBreakListener;
import me.tofpu.speedbridge.game.listener.functionality.EntityDamageListener;
import me.tofpu.speedbridge.game.listener.functionality.FoodLevelChangeListener;
import me.tofpu.speedbridge.game.listener.machanic.BlockPlaceListener;
import me.tofpu.speedbridge.game.listener.machanic.PlayerInteractListener;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.util.UpdateChecker;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class SpeedBridge extends JavaPlugin {
    private final List<Listener> listeners;
    private final Game game;

    public SpeedBridge() {
        this.game = new Game(this);

        final DataManager dataManager = game().dataManager();
        final UserService userService = game().userService();
        final IslandService islandService = game().islandService();
        final GameService gameService = game().gameService();
        final LobbyService lobbyService = game().lobbyService();

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

    @Override
    public void onEnable() {
        initialize();
        load();
    }

    @Override
    public void onDisable() {
        game().dataManager().save();

        game().lobbyService().getLeaderboard().cancel();
    }

    private void initialize(){
        UpdateChecker.init(this, 95918).requestUpdateCheck().whenComplete((updateResult, throwable) -> {
            if (updateResult.getReason() == UpdateChecker.UpdateReason.NEW_UPDATE) {
                getLogger().warning("You're not on the latest version of SpeedBridge!");
                getLogger().warning("It's highly recommended to download the latest version at https://www.spigotmc.org/resources/speedbridge-1-free-bridge-trainer-rpf.95918/!");
            } else if (updateResult.getReason() == UpdateChecker.UpdateReason.UP_TO_DATE) {
                getLogger().warning("You're using the latest version of SpeedBridge!");
            }
        });

        final DataManager dataManager = game().dataManager();
        // TODO: REMOVING THIS SOON
        game().initialize();

        // initializes the files
        dataManager.initialize(game().islandService(), game().userService(), game().lobbyService(), this, getDataFolder());

        final ConfigIdentifier settingsIdentifier = ConfigIdentifier.of(
                "settings",
                dataManager.getPluginFiles()[0].configuration()
        );
        final ConfigIdentifier messagesIdentifier = ConfigIdentifier.of(
                "messages",
                dataManager.getPluginFiles()[1].configuration()
        );

        ConfigAPI.initialize(settingsIdentifier, messagesIdentifier);
        ModeManager.getModeManager().initialize();

        initializePlaceholderApi();
        initializeListeners();
    }

    private void load(){
        game.dataManager().load();
        // RELOAD BUG FIX
        Bukkit.getOnlinePlayers().forEach(player -> game().dataManager().loadUser(player.getUniqueId()));
        // starting the leaderboard
        game().lobbyService().getLeaderboard().start(this);
    }

    private void initializePlaceholderApi() {
        // registering the placeholder dependency
        DependencyAPI.register(new PlaceholderDependency());

        // initializing the dependency api
        DependencyAPI.initialize(this);

        // if it's not available, return
        if (!DependencyAPI.get("PlaceholderAPI").isAvailable()) return;
        Util.isPlaceholderHooked = true;
//        getLogger().info("Hooked into PlaceholderAPI");
        new BridgeExpansion(getDescription(), game().userService(), game().gameService(), game().lobbyService()).register();
    }

    private void initializeListeners() {
        final PluginManager manager = Bukkit.getPluginManager();
        for (final Listener listener : this.listeners) {
            manager.registerEvents(listener, this);
        }
    }

    public Game game() {
        return this.game;
    }
}
