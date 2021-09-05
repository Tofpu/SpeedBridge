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

import java.util.Map;

public class GameRunnable extends BukkitRunnable {
    private final Plugin plugin;
    private final GameService gameService;
    private final Map<User, Island> gameChecker;

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
            final TwoSection selection = (TwoSection) entry.getValue().properties().get("selection");

            final Cuboid cuboid = Cuboid.of(selection.pointA(), selection.pointB());
            if (!cuboid.isIn(player.getLocation())) {
                this.gameService.reset(user);
            }
        }
    }

    public void start(){
        if (!isPaused()) return;

        this.paused = false;
        runTaskTimer(plugin, 1L, 10L);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        this.paused = true;
    }

    public boolean isPaused() {
        return paused;
    }
}
