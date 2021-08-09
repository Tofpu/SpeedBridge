package me.tofpu.speedbridge.game;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.game.service.impl.GameService;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.island.service.impl.IslandService;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.lobby.service.impl.LobbyService;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.user.service.impl.UserService;

import java.io.File;

public class Game {
    private final IIslandService islandService;
    private final IUserService userService;

    private final ILobbyService lobbyService;

    private final GameController gameController;
    private final IGameService gameService;

    private final DataManager dataManager;

    public Game(final File directory) {
        this.islandService = new IslandService();
        this.userService = new UserService();

        this.lobbyService = new LobbyService();

        this.gameController = new GameController(islandService);
        this.gameService = new GameService(islandService, userService, lobbyService);

        this.dataManager = new DataManager(directory, islandService, userService, lobbyService);
    }

    public IIslandService getIslandService() {
        return islandService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public GameController getGameController() {
        return gameController;
    }

    public IGameService getGameService() {
        return gameService;
    }

    public ILobbyService getLobbyService() {
        return lobbyService;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
