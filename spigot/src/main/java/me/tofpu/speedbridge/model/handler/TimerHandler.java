package me.tofpu.speedbridge.model.handler;

import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.object.user.timer.Timer;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.model.object.user.properties.timer.TimerFactory;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.process.ProcessType;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TimerHandler {
    private final GameHandler gameHandler;
    private final Map<UUID, Timer> timers;

    public TimerHandler(final GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        timers = new HashMap<>();
    }

    public void updateTimer(final User user) {
        if (user == null) {
            return;
        }
        Process.GAME_UPDATE.process(gameHandler, user, ProcessType.PROCESS);
    }

    public void resetTimer(final User user) {
        final Island island = gameHandler.islandService().findIslandBy(IslandRepository.SearchAlgorithm
                .of(user.properties().islandSlot())).get();

        timers.remove(user.uniqueId());
        gameHandler.resetBlocks(island);
    }

    public void initiateTimer(final User user) {
        this.timers.put(user.uniqueId(), TimerFactory.of(user.properties().islandSlot()));
    }

    public Optional<Timer> obtainTimer(final UUID uniqueId) {
        return Optional.ofNullable(timers.get(uniqueId));
    }

    public boolean isTimerTicking(final UUID uniqueId) {
        return this.timers.containsKey(uniqueId);
    }

    public Map<UUID, Timer> timers() {
        return timers;
    }
}
