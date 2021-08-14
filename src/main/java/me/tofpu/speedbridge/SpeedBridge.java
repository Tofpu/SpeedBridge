package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandHandler;
import me.tofpu.speedbridge.command.old.CommandManagerOld;
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

// TODO: Complete all the to-do's
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

        getGame().getLobbyService().getLeaderboard().initialize();

        DependencyRegister.loadAll(this);
        dataManager.load();

        registerPlaceholderApi();
        registerListeners();
        new CommandHandler(getGame(), this);
//        getCommand("speedbridge").setExecutor(new CommandManagerOld(getGame().getGameController(), getGame().getUserService(), getGame().getGameService(), getGame().getLobbyService()));

        // RELOAD BUG FIX
        Bukkit.getOnlinePlayers().forEach(player -> dataManager.loadUser(player.getUniqueId()));
    }

    @Override
    public void onDisable() {
        final DataManager dataManager = this.game.getDataManager();
        dataManager.save();

        getGame().getLobbyService().getLeaderboard().cancel();
    }

    public void registerPlaceholderApi() {
        if (DependencyRegister.get("PlaceholderAPI").getDependency() == null) return;
        getLogger().info("Hooked into PlaceholderAPI");
        new BridgeExpansion(getDescription(), getGame().getUserService(), game.getGameService()).register();
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
