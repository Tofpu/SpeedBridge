package me.tofpu.speedbridge.game;

import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.game.service.impl.GameService;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.island.service.impl.IslandService;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.user.service.impl.UserService;

public class Game {
    private final IIslandService islandService;
    private final IUserService userService;
    private final IGameService gameService;

    public Game() {
        this.islandService = new IslandService();
        this.userService = new UserService();
        this.gameService = new GameService(islandService, userService);
    }

    public IIslandService getIslandService() {
        return islandService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public IGameService getGameService() {
        return gameService;
    }
}
