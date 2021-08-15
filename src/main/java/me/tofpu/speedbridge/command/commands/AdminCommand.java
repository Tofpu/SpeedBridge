package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.annotation.*;
import me.tofpu.speedbridge.command.commands.base.BridgeBaseCommand;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

@CommandAlias("island")
public class AdminCommand extends BridgeBaseCommand {
    private final ILobbyService lobbyService;
    private final GameController controller;

    public AdminCommand(final ILobbyService lobbyService, final GameController controller) {
        super("island");
        this.lobbyService = lobbyService;
        this.controller = controller;
    }

    @Override
    @Private
    @Subcommand("help")
    @CommandPermission("island.help")
    @Description("Shows you all the available commands")
    public void onHelp(Player player) {
        super.onHelp(player);
    }


    @Subcommand("create")
    @CommandPermission("island.create")
    @Syntax("<slot>")
    @Description("Creates an island in that particular slot")
    public void onCreate(final Player player, int slot) {
        controller.createIsland(player, slot);
        Util.message(player, Path.MESSAGES_ISLAND_CREATION);
    }

    @Subcommand("set")
    @CommandPermission("island.set")
    @CommandCompletion("@setupStage")
    @Syntax("spawn/point/selection/selection-b/lobby")
    @Description("Set the island locations")
    public void onSet(final Player player, final String arg) {
        final SetupStage stage;
        if ((stage = SetupStage.getMatch(arg)) == null) {
            // TODO: YOU CAN ONLY SET LOCATIONS TO SPAWN/POINT/SELECTION-A/SELECTION-B MESSAGE
            return;
        }
        if (stage == SetupStage.LOBBY) {
            lobbyService.setLobbyLocation(player.getLocation());
            Util.message(player, Path.MESSAGES_LOBBY_LOCATION);
            return;
        }
        final Result result = controller.setupIsland(player, stage);

        switch (result) {
            case SUCCESS:
                Util.message(player, Path.MESSAGES_ISLAND_CREATION);
                break;
            case DENY:
                Util.message(player, Path.MESSAGES_INVALID_ISLAND);
                break;
        }
    }

    @Subcommand("finish")
    @CommandPermission("island.finish")
    @Description("The island becomes available if the setup is completed")
    public void onFinish(final Player player) {
        final Result result = controller.finishSetup(player);

        switch (result) {
            case SUCCESS:
                Util.message(player, Path.MESSAGES_ISLAND_COMPLETED);
                break;
            case DENY:
                Util.message(player, Path.MESSAGES_ISLAND_INCOMPLETE);
        }
    }
}
