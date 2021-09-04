package me.tofpu.speedbridge;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.identifier.ConfigIdentifier;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.github.requestpluginsforfree.dependency.impl.PlaceholderDependency;
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
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.user.service.UserService;
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

        final DataManager dataManager = getGame().getDataManager();
        final UserService userService = getGame().getUserService();
        final IslandService islandService = getGame().getIslandService();
        final GameService gameService = getGame().getGameService();
        final LobbyService lobbyService = getGame().getLobbyService();

        this.listeners = Arrays.asList(
                new PlayerJoinListener(lobbyService, dataManager),
                new PlayerQuitListener(userService, gameService, dataManager),
                new PlayerInteractListener(userService, islandService, gameService),
                new BlockPlaceListener(userService, islandService, gameService),
                new BlockBreakListener(userService, islandService, gameService),
                new EntityDamageListener(gameService),
                new FoodLevelChangeListener(gameService));
    }

    @Override
    public void onEnable() {
        initialize();
        load();
    }

    @Override
    public void onDisable() {
        getGame().getDataManager().save();

        getGame().getLobbyService().getLeaderboard().cancel();
    }

    private void initialize(){
        final DataManager dataManager = getGame().getDataManager();
        // TODO: REMOVING THIS SOON
        getGame().initialize();

        // initializes the files
        dataManager.initialize(this, getDataFolder());

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
        game.getDataManager().load();
        // RELOAD BUG FIX
        Bukkit.getOnlinePlayers().forEach(player -> getGame().getDataManager().loadUser(player.getUniqueId()));
        // starting the leaderboard
        getGame().getLobbyService().getLeaderboard().start(this);
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
        new BridgeExpansion(getDescription(), getGame().getUserService(), getGame().getGameService(), getGame().getLobbyService()).register();
    }

    private void initializeListeners() {
        final PluginManager manager = Bukkit.getPluginManager();
        for (final Listener listener : this.listeners) {
            manager.registerEvents(listener, this);
        }
    }

    public Game getGame() {
        return this.game;
    }
}
