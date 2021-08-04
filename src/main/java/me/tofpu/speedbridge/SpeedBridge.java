package me.tofpu.speedbridge;

import me.tofpu.speedbridge.command.CommandManager;
import me.tofpu.speedbridge.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

// TODO:
// Add a game package, game service & game controller! (DONE)
// Handle commands (DONE)
// Complete all the to-do's
// Create a queue system (refer to discord requester for more info)
// Listen to PlayerJoinEvent, PlayerLeaveEvent & PlayerPressurePlate or whatever, to load & de-load data, and for getting timed.
// Save Islands & Users data (USE GSON)
// Have the island & user controllers load data instead!
public final class SpeedBridge extends JavaPlugin {
    private final Game game;

    public SpeedBridge() {
        this.game = new Game();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("speedbridge").setExecutor(new CommandManager(this.game.getGameController(), this.game.getGameService()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Game getGame() {
        return game;
    }
}
