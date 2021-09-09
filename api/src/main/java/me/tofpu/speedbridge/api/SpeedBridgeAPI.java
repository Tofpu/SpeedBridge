package me.tofpu.speedbridge.api;

import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.UserService;

import java.util.logging.Logger;

public final class SpeedBridgeAPI {
    private static final SpeedBridgeAPI SPEED_BRIDGE_API = new SpeedBridgeAPI();

    private static UserService userService;
    private static IslandService islandService;
    private static GameService gameService;
    private static LobbyService lobbyService;

    private static Logger logger;

    public static void setInstance(
            final Logger logger,
            final UserService userService,
            final IslandService islandService,
            final GameService gameService,
            final LobbyService lobbyService) {
        if (SpeedBridgeAPI.userService != null
                || SpeedBridgeAPI.islandService != null
                || SpeedBridgeAPI.gameService != null
                || SpeedBridgeAPI.lobbyService != null){
            throw new UnsupportedOperationException("You cannot initialize neither of the services twice.");
        }
        SpeedBridgeAPI.logger = logger;
        SpeedBridgeAPI.userService = userService;
        SpeedBridgeAPI.islandService = islandService;
        SpeedBridgeAPI.gameService = gameService;
        SpeedBridgeAPI.lobbyService = lobbyService;

        // TEMPORALLY
        logger.info("The SpeedBridgeAPI has been initialized!");
    }

    private SpeedBridgeAPI() {}

    public static UserService getUserService() {
        return userService;
    }

    public static IslandService getIslandService() {
        return islandService;
    }

    public static GameService getGameService() {
        return gameService;
    }

    public static LobbyService getLobbyService() {
        return lobbyService;
    }
}
