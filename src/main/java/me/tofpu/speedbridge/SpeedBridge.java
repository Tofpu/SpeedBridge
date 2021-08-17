package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandHandler;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.config.Config;
import me.tofpu.speedbridge.data.listener.PlayerJoinListener;
import me.tofpu.speedbridge.data.listener.PlayerQuitListener;
import me.tofpu.speedbridge.dependency.register.DependencyRegister;
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
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

// TODO: Complete all the to-do's
public final class SpeedBridge extends JavaPlugin {
    private final List<Listener> listeners;

    private final Game game;

    public SpeedBridge() {
        this.game = new Game(this, getDataFolder());

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
        Config.initialize(this);

        dataManager.initialize();
        ModeManager.getModeManager().initialize();

        DependencyRegister.loadAll(this);
        dataManager.load();

        initializePlaceholderApi();
        initializeListeners();
        new CommandHandler(getGame(), this);

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

    public void initializePlaceholderApi() {
        if (DependencyRegister.get("PlaceholderAPI").getDependency() == null) return;
        getLogger().info("Hooked into PlaceholderAPI");
        new BridgeExpansion(getDescription(), getGame().getUserService(), getGame().getGameService(), getGame().getLobbyService()).register();
    }

    public void initializeListeners() {
        final PluginManager manager = Bukkit.getPluginManager();
        for (final Listener listener : listeners) {
            manager.registerEvents(listener, this);
        }
    }

    public Game getGame() {
        return game;
    }
}
