package me.tofpu.speedbridge;

import me.tofpu.speedbridge.game.Game;
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
        game().dataManager().shutdown();

        // cancelling the leaderboard task
        game().lobbyService().getLeaderboard().cancel();
    }

    public Game game() {
        return this.game;
    }
}
