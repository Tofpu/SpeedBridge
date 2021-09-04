package me.tofpu.speedbridge.game;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.game.service.impl.GameServiceImpl;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.island.service.impl.IslandServiceImpl;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.lobby.service.impl.LobbyServiceImpl;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.user.service.impl.UserServiceImpl;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Game {
    private final IslandService islandService;
    private final UserService userService;

    private final LobbyService lobbyService;

    private final GameController gameController;
    private final GameService gameService;

    private final DataManager dataManager;

    public Game(final Plugin plugin) {
        this.islandService = new IslandServiceImpl();
        this.userService = new UserServiceImpl();

        this.lobbyService = new LobbyServiceImpl();

        this.gameController = new GameController(islandService);
        this.gameService = new GameServiceImpl(plugin, islandService, userService, lobbyService);

        this.dataManager = new DataManager(islandService, userService, lobbyService);
    }

    public IslandService getIslandService() {
        return islandService;
    }

    public UserService getUserService() {
        return userService;
    }

    public GameController getGameController() {
        return gameController;
    }

    public GameService getGameService() {
        return gameService;
    }

    public LobbyService getLobbyService() {
        return lobbyService;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
