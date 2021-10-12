package me.tofpu.speedbridge.model.service;

import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.service.GameService;
import me.tofpu.speedbridge.api.model.service.IslandService;
import me.tofpu.speedbridge.api.model.service.LobbyService;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.service.UserService;
import me.tofpu.speedbridge.api.model.object.user.timer.Timer;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.model.handler.GameHandler;
import me.tofpu.speedbridge.model.handler.SpectatorHandler;
import me.tofpu.speedbridge.model.handler.TimerHandler;
import me.tofpu.speedbridge.model.object.leaderboard.LeaderboardServiceImpl;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.model.object.game.runnable.GameRunnable;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

public class GameServiceImpl implements GameService {
    private final GameHandler handler;
    private final TimerHandler timerHandler;
    private final SpectatorHandler spectatorHandler;

    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;
    private final LeaderboardServiceImpl leaderboardService;

    private final GameRunnable runnable;

    public GameServiceImpl(final GameHandler handler, final TimerHandler timerHandler, final SpectatorHandler spectatorHandler, final Plugin plugin, final IslandService islandService, final UserService userService, final LobbyService lobbyService, final LeaderboardServiceImpl leaderboardService) {
        this.handler = handler;
        this.timerHandler = timerHandler;
        this.spectatorHandler = spectatorHandler;
        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.leaderboardService = leaderboardService;

        this.runnable = new GameRunnable(plugin, this);
    }

    @Override
    public Result join(final JoinAlgorithm algorithm,
            final Player player) {
        return handler.join(algorithm, player);
    }

    @Override
    public Result spectate(final Player issuer, final Player target) {
        final User userIssuer = userService.findOrDefault(issuer
                .getUniqueId());
        final User userTarget = userService.findOrDefault(target.getUniqueId());

        return spectatorHandler.spectate(userIssuer, userTarget);
    }

    @Override
    public Result leave(final Player player) {
        return handler.leave(player);
    }

    public void messageSpectator(final User target, final String message,
            boolean includeTarget) {
        spectatorHandler.messageSpectator(target, message, includeTarget);
    }

    @Override
    public boolean isPlaying(final Player player) {
        final User user;
        // checking if the user instance is null
        if ((user = userService.findOrDefault(player.getUniqueId())) == null) {
            // since we have no user instance associated with
            // this player unique id, we're returning false
            return false;
        }
        return handler.isPlaying(user);
    }

    @Override
    public boolean isSpectating(final Player player) {
        final User user;
        // checking if the user instance is null
        if ((user = userService.findOrDefault(player.getUniqueId())) == null) {
            // since we have no user instance associated with
            // this player unique id, we're returning false
            return false;
        }
        return spectatorHandler.isSpectating(user);
    }

    @Override
    public void addTimer(final User user) {
        timerHandler.initiateTimer(user);
    }

    @Override
    public boolean hasTimer(final User user) {
        return timerHandler.isTimerTicking(user.uniqueId());
    }

    @Override
    public Optional<Timer> getTimer(User user) {
        return timerHandler.obtainTimer(user.uniqueId());
    }

    @Override
    public void updateTimer(final User user) {
        timerHandler.updateTimer(user);
    }

    @Override
    public void resetTimer(final User user) {
        timerHandler.resetTimer(user);
    }

    @Override
    public void reset(final User user) {
        handler.reset(user);
    }

    @Override
    public void resetBlocks(final Island island) {
        handler.resetBlocks(island);
    }

    @Override
    public void resetIsland(final int slot) {
        handler.resetIsland(slot);
    }

    public LobbyService lobbyService() {
        return lobbyService;
    }

    public LeaderboardServiceImpl leaderboardService() {
        return leaderboardService;
    }

    public Map<UUID, Timer> gameTimer() {
        return timerHandler.timers();
    }

    public Map<User, Island> players() {
        return handler.players();
    }

    public GameRunnable runnable() {
        return runnable;
    }

    public Map<User, Island> spectators() {
        return spectatorHandler.spectators();
    }
}
