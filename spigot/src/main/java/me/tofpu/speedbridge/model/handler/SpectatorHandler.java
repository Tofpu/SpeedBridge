package me.tofpu.speedbridge.model.handler;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SpectatorHandler {
    private final GameHandler gameHandler;
    private final Map<User, Island> spectators;

    @Inject
    public SpectatorHandler(final GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.spectators = new ConcurrentHashMap<>();
    }

    public void messageSpectator(final User target, final String message,
            boolean includeTarget) {
        // if includeTarget is true, message the target too!
        if (includeTarget) {
            target.player().sendMessage(Util.colorize(message));
        }

        // looping through the spectators of this target
        for (final Map.Entry<User, Island> entry : spectators.entrySet()) {
            if (entry.getValue().slot() != target.properties().islandSlot()) continue;
            final User userSpectator = entry.getKey();

            final Player spectator = userSpectator.player();
            if (spectator == null) {
                continue;
            }

            // messaging the spectator
            spectator.sendMessage(Util.colorize(message));
        }
    }

    public Result spectate(final User issuer, final User target) {
        if (gameHandler.isPlaying(issuer) || isSpectating(issuer)) {
            return Result.BUSY;
        }
        if (!gameHandler.isPlaying(target)) {
            return Result.FAIL;
        }
        final Optional<Island> optionalIsland = gameHandler.islandService()
                .findIslandBy(IslandRepository.SearchAlgorithm.of(target.properties()
                        .islandSlot()));
        final Island island =
                optionalIsland.orElseThrow(() -> new IllegalStateException("Target's " + "Island was null!"));

        Process.GAME_SPECTATOR.process(gameHandler, island.location(),
                ProcessType.PROCESS, issuer, target);
        spectators.put(issuer, island);
        return Result.SUCCESS;
    }

    public Result leaveSpectate(final User user) {
        final Optional<Island> optionalIsland = Optional.ofNullable(spectators.get(user));
        if (!isSpectating(user) || !optionalIsland.isPresent()) {
            return Result.FAIL;
        }

        Process.GAME_SPECTATOR.process(gameHandler,
                gameHandler.lobbyService().lobby().location().get(),
                ProcessType.REVERSE, user, optionalIsland.get().takenBy());
        spectators.remove(user);
        return Result.SUCCESS;
    }

    public boolean isSpectating(final User user) {
        return spectators.containsKey(user);
    }

    public Map<User, Island> spectators() {
        return spectators;
    }
}
