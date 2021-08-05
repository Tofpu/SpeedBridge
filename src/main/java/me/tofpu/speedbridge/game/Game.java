package me.tofpu.speedbridge.game;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.game.service.impl.GameService;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.island.service.impl.IslandService;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.user.service.impl.UserService;

import java.io.File;

public class Game {
    private final IIslandService islandService;
    private final IUserService userService;

    private final GameController gameController;
    private final IGameService gameService;

    private final DataManager dataManager;

    public Game(@NotNull final File directory) {
        this.islandService = new IslandService();
        this.userService = new UserService();

        this.gameController = new GameController(islandService);
        this.gameService = new GameService(islandService, userService);

        this.dataManager = new DataManager(directory, islandService, userService);
    }

    @NotNull
    public IIslandService getIslandService() {
        return islandService;
    }

    @NotNull
    public IUserService getUserService() {
        return userService;
    }

    @NotNull
    public GameController getGameController() {
        return gameController;
    }

    @NotNull
    public IGameService getGameService() {
        return gameService;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
