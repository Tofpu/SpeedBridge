package me.tofpu.speedbridge.game.runnable;

import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.point.TwoSection;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.util.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class GameRunnable implements Runnable {
    private final Plugin plugin;
    private final GameService gameService;
    private final Map<User, Island> gameChecker;

    private BukkitTask task;
    private boolean paused = true;

    public GameRunnable(final Plugin plugin, final GameService gameService, final Map<User, Island> gameChecker) {
        this.plugin = plugin;
        this.gameService = gameService;
        this.gameChecker = gameChecker;
    }

    @Override
    public void run() {
        for (final Map.Entry<User, Island> entry : gameChecker.entrySet()) {
            final User user = entry.getKey();
            final Player player;
            if (user == null || (player = Bukkit.getPlayer(user.uniqueId())) == null) {
                // TODO: not handling the quits properly?
                return;
            }
            final TwoSection selection = (TwoSection) entry.getValue().properties().get("position");

            final Cuboid cuboid = Cuboid.of(selection.pointA(), selection.pointB());
            if (!cuboid.isIn(player.getLocation())) {
                this.gameService.reset(user);
            }
        }
    }

    public void resume() {
        if (!isPaused()) return;
        this.paused = false;

        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, 1L, 10L);
    }

    public void pause() {
        this.paused = true;
        this.task.cancel();
    }

    public BukkitTask getTask() {
        return task;
    }

    public boolean isPaused() {
        return paused;
    }
}
