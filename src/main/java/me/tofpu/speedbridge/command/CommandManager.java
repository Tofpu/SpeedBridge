package me.tofpu.speedbridge.command;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.IGameService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    private final GameController gameController;
    private final IGameService gameService;

    public CommandManager(@NotNull final GameController gameController, @NotNull final IGameService gameService) {
        this.gameController = gameController;
        this.gameService = gameService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player player = (Player) sender;

        if (args.length < 1) return false;
        switch (args[0]) {
            case "join":
                // TODO: HAVE A CHECK LATER
                final Result joinResult = gameService.join(player);
                if (joinResult == Result.DENY) {
                    //TODO: SEND MESSAGE, THAT THEY'VE ALREADY JOINED!
                } else if (joinResult == Result.FULL) {
                    //TODO: SEND MESSAGE, THAT THERE IS NO AVAILABLE ISLANDS!
                }
                break;
            case "leave":
                // TODO: HAVE A CHECK LATER
                final Result leaveResult = gameService.leave(player);
                if (leaveResult == Result.DENY) {
                    //TODO: SEND MESSAGE, THAT THEY'VE NOT JOINED AN ISLAND YET!
                }
                break;
            case "create":
                if (args.length < 2) return false;
                final Integer createSlot = tryParse(args[1]);
                if (createSlot == null) {
                    //TODO: SEND MESSAGE SAYING YOU HAVE TO INSERT NUMBERS ONLY!
                    return false;
                }

                gameController.createIsland(player, createSlot);
                //TODO: SEND MESSAGE SAYING SUCCESS!
                break;
            case "set":
                // /speedbridge set (slot) spawn/point-a/point-b
                if (args.length < 3) return false;
                final Integer setSlot = tryParse(args[1]);
                if (setSlot == null) {
                    //TODO: SEND MESSAGE SAYING YOU HAVE TO INSERT NUMBERS ONLY!
                    return false;
                }

                switch (this.gameController.setupIsland(player, SetupStage.valueOf(args[2].toUpperCase().replace("_", "-")))) {
                    case SUCCESS:
                        //TODO: SEND MESSAGE SAYING SUCCESS!
                        break;
                    case DENY:
                        //TODO: SEND MESSAGE SAYING THEY HAVE NOT CREATED AN ISLAND!
                        break;
                }
            case "finish":
                final Result finishResult = this.gameController.finishSetup(player);
                if (finishResult == Result.SUCCESS) {
                    //TODO: SEND MESSAGE SAYING IT'S BEEN COMPLETED!
                } else if (finishResult == Result.DENY) {
                    //TODO: SEND MESSAGE SAYING THEY EITHER HAVE NOT CREATED AN ISLAND OR HAVE NOT SETUP ALL THE SPAWN POINTS!
                }
                break;
        }
        return false;
    }

    public Integer tryParse(final String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
