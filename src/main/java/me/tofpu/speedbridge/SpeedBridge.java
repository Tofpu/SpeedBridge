package me.tofpu.speedbridge;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.identifier.ConfigIdentifier;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.github.requestpluginsforfree.dependency.impl.PlaceholderDependency;
import me.tofpu.speedbridge.command.CommandHandler;
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
import org.bstats.bukkit.Metrics;
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
        initialize(dataManager);

        // RELOAD BUG FIX
        Bukkit.getOnlinePlayers().forEach(player -> dataManager.loadUser(player.getUniqueId()));

        getGame().getLobbyService().getLeaderboard().initialize(this);
    }

    @Override
    public void onDisable() {
        final DataManager dataManager = this.game.getDataManager();
        dataManager.save();

        getGame().getLobbyService().getLeaderboard().cancel();
    }

    private void initialize(final DataManager dataManager){
        // initializes the files
        dataManager.initialize(this, getDataFolder());
        dataManager.load();

        final ConfigIdentifier settingsIdentifier = ConfigIdentifier.of("settings", dataManager.getPluginFiles()[0].configuration());
        final ConfigIdentifier messagesIdentifier = ConfigIdentifier.of("messages", dataManager.getPluginFiles()[1].configuration());
        ConfigAPI.initialize(settingsIdentifier, messagesIdentifier);

        ModeManager.getModeManager().initialize();

        initializePlaceholderApi();
        initializeListeners();

        new CommandHandler(getGame(), this);
        new Metrics(this, 12679);
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
