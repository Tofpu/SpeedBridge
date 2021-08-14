package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

@CommandAlias("island")
public class AdminCommand extends BaseCommand {
    private final GameController controller;

    public AdminCommand(GameController controller) {
        this.controller = controller;
    }

    @Subcommand("create")
    @Syntax("<slot>")
    public void onCreate(final Player player, int slot){
        controller.createIsland(player, slot);
        Util.message(player, Path.MESSAGES_ISLAND_CREATION);
    }

    @Subcommand("set")
    @Syntax("<spawn>|<point>/<selection-a>/<selection-b>")
    public void onSet(final Player player, String arg){
        final Result result = controller.setupIsland(player, SetupStage.valueOf(arg.toUpperCase().replace("-", "_")));

        switch (result){
            case SUCCESS:
                Util.message(player, Path.MESSAGES_ISLAND_CREATION);
                break;
            case DENY:
                Util.message(player, Path.MESSAGES_INVALID_ISLAND);
                break;
        }
    }

    @Subcommand("finish")
    public void onFinish(final Player player){
        final Result result = controller.finishSetup(player);

        switch (result){
            case SUCCESS:
                Util.message(player, Path.MESSAGES_ISLAND_COMPLETED);
                break;
            case DENY:
                Util.message(player, Path.MESSAGES_ISLAND_INCOMPLETE);
        }
    }
}
