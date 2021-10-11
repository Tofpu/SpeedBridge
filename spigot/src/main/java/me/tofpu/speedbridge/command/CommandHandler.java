package me.tofpu.speedbridge.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandManager;
import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.api.game.SetupStage;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.command.sub.AdminCommand;
import me.tofpu.speedbridge.command.sub.MainCommand;
import me.tofpu.speedbridge.model.object.game.Game;
import me.tofpu.speedbridge.model.service.ModeManager;
import me.tofpu.speedbridge.util.Util;

public class CommandHandler {
    private final BukkitCommandManager commandManager;

    public CommandHandler(Game game, final SpeedBridge plugin) {
        this.commandManager = new BukkitCommandManager(plugin);

        // contexts
        this.commandManager.getCommandContexts().registerContext(Mode.class, (string) -> {
            final String input = string.popFirstArg();

            return ModeManager.getModeManager().get(input);
        });

        // completions
        this.commandManager.getCommandCompletions().registerCompletion("modes", context -> Util.toString(ModeManager.getModeManager().modes()));
        this.commandManager.getCommandCompletions().registerCompletion("availableIslands", context -> Util.toString(game.islandService().getAvailableIslands()));
        this.commandManager.getCommandCompletions().registerCompletion("setupStage", context -> Util.toString(SetupStage.values()));

        // registrations
        registerCommand(new MainCommand(game.userService(), game.gameService(), game.lobbyService(), game.leaderboardManager()));
        registerCommand(new AdminCommand(plugin, game.islandService(), game.lobbyService(), game.gameService(), game.gameController(), game.dataManager()));
    }

    public void registerCommand(final BaseCommand command) {
        this.commandManager.registerCommand(command);
    }
}
