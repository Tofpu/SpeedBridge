package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandManager;
import me.tofpu.speedbridge.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

// TODO:
// Add a game package, game service & game controller!
// Have the island & user controllers load data instead!
public final class SpeedBridge extends JavaPlugin {
    private final Game game;

    public SpeedBridge(){
        this.game = new Game();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("speedbridge").setExecutor(new CommandManager(this.game.getGameService()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Game getGame() {
        return game;
    }
}
