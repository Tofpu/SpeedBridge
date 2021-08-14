package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.RegisteredCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.google.common.collect.Maps;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

import java.util.Map;

@CommandAlias("island")
public class AdminCommand extends BaseCommand {
    private final GameController controller;

    public AdminCommand(GameController controller) {
        this.controller = controller;
    }

    @Subcommand("help")
    @Description("Shows you all the available commands")
    public void onHelp(final Player player){
        player.sendMessage(Util.colorize("&e&l&m<&6&m------&r &e&lSpeedBridge Commands &6&m------&e&l&m>"));
        for (final Map.Entry<String, RegisteredCommand> test : getSubCommands().entries()){
            player.sendMessage(Util.format(test.getValue()));
        }
        player.sendMessage(Util.colorize("&e&l&m<&r&6&m----------------&e&l&m>&r &e&l&m<&r&6&m----------------&e&l&m>"));
    }

    @Subcommand("create")
    @Syntax("<slot>")
    @Description("Creates an island in that particular slot")
    public void onCreate(final Player player, int slot){
        controller.createIsland(player, slot);
        Util.message(player, Path.MESSAGES_ISLAND_CREATION);
    }

    @Subcommand("set")
    @Syntax("<spawn>|<point>/<selection-a>/<selection-b>")
    @Description("Set the island locations")
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
    @Description("The island becomes available if the setup is completed")
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
