package me.tofpu.speedbridge.model.object.game.runnable;

import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.point.TwoSection;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.util.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class GameRunnable implements Runnable {
    private final Plugin plugin;
    private final GameService gameService;

    private final Map<User, Island>[] maps;

    private BukkitTask task;
    private boolean paused = true;

    public GameRunnable(final Plugin plugin, final GameService gameService, final Map<User, Island> players, final Map<User, Island> spectators) {
        this.plugin = plugin;
        this.gameService = gameService;

        this.maps = new Map[2];

        this.maps[0] = players;
        this.maps[1] = spectators;
    }

    private void check(final Map.Entry<User, Island> entry, boolean spectate) {
        final User user = entry.getKey();
        final Player player;
        if (user == null || (player = Bukkit.getPlayer(user.uniqueId())) == null) {
            // shouldn't happen, else not handling the quits properly
            return;
        }
        final TwoSection selection = (TwoSection) entry.getValue().properties().get("position");

        final Cuboid cuboid = Cuboid.of(selection.pointA(), selection.pointB());
        if (!cuboid.isIn(player.getLocation())) {
            if (!spectate) {
                this.gameService.reset(user);
            } else {
                final Island island = entry.getValue();
                player.teleport(island.location());
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < maps.length; i++) {
            final Map<User, Island> map = maps[i];

            for (final Map.Entry<User, Island> entry : map.entrySet()) {
                check(entry, i == 0);
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
