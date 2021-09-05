package me.tofpu.speedbridge.game;

import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.command.CommandHandler;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.game.service.GameServiceImpl;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.island.service.IslandServiceImpl;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.lobby.service.LobbyServiceImpl;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.user.service.UserServiceImpl;
import org.bstats.bukkit.Metrics;

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

    public Game(final SpeedBridge plugin) {
        this.speedBridge = plugin;
        this.dataManager = new DataManager();

        this.islandService = new IslandServiceImpl(dataManager);
        this.userService = new UserServiceImpl(dataManager);

        this.lobbyService = new LobbyServiceImpl();

        this.gameController = new GameController(islandService);
        this.gameService = new GameServiceImpl(plugin, islandService, userService, lobbyService);
    }

    public void initialize(){
        new CommandHandler(this, speedBridge);
        new Metrics(speedBridge, 12679);

        getIslandService().initialize(getDataManager());
    }

    public IslandService getIslandService() {
        return this.islandService;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public GameService getGameService() {
        return this.gameService;
    }

    public LobbyService getLobbyService() {
        return this.lobbyService;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }
}
