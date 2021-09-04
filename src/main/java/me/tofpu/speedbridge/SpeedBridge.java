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
import me.tofpu.speedbridge.util.UpdateChecker;
import me.tofpu.speedbridge.util.Util;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        UpdateChecker.init(this, 95918).requestUpdateCheck().whenComplete((updateResult, throwable) -> {
            if (updateResult.getReason() == UpdateChecker.UpdateReason.NEW_UPDATE) {
                getLogger().warning("You're not on the latest version of SpeedBridge!\nIt's highly recommended to download the latest version at https://www.spigotmc.org/resources/speedbridge-1-free-bridge-trainer-rpf.95918/!");
            } else if (updateResult.getReason() == UpdateChecker.UpdateReason.UP_TO_DATE) {
                getLogger().warning("You're using the latest version of SpeedBridge!");
            }
        });

        final DataManager dataManager = getGame().getDataManager();
        Config.initialize(this);

        dataManager.initialize();
        ModeManager.getModeManager().initialize();

        DependencyRegister.loadAll(this);
        dataManager.load();

        initializePlaceholderApi();
        initializeListeners();
        new CommandHandler(getGame(), this);

        new Metrics(this, 12679);

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
        Util.isPlaceholderHooked = true;
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
