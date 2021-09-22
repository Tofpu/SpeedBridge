package me.tofpu.speedbridge;

import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedBridge extends JavaPlugin {
    private final Game game;

    public SpeedBridge() {
        this.game = new Game(this);
    }

    @Override
    public void onLoad() {
        // initializing phase
        game.initialize();
    }

    @Override
    public void onEnable() {
        // loading phase
        this.game.load();
    }

    @Override
    public void onDisable() {
        // saving phase
        Game.EXECUTOR.shutdown();
        game().dataManager().shutdown();

        // cancelling the leaderboard task
        game.leaderboardManager().compute(null, leaderboard -> ((AbstractLeaderboard) leaderboard).cancel());
    }

    public Game game() {
        return this.game;
    }
}
