package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.annotation.*;
import me.tofpu.speedbridge.command.commands.base.BridgeBaseCommand;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.expansion.type.ExpansionType;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Arrays;
import java.util.Locale;

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
    @CommandPermission("island.info")
    @Description("Shows you all the available commands")
    public void onHelp(Player player) {
        super.onHelp(player);
    }

    @Override
    @Private
    @CatchUnknown
    @CommandPermission("island.info")
    public void onUnknownCommand(Player player) {
        super.onUnknownCommand(player);
    }

    @Subcommand("create")
    @CommandPermission("island.create")
    @Syntax("<slot>")
    @Description("Creates an island in that particular slot")
    public void onCreate(final Player player, int slot) {
        final Result result = controller.createIsland(player, slot);

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_CREATION;
                break;
            case DENY:
                path = Path.MESSAGES_ISLAND_EXISTS;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
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

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_CREATION;
                break;
            case DENY:
                path = Path.MESSAGES_INVALID_ISLAND;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("finish")
    @CommandPermission("island.finish")
    @Description("The island becomes available if the setup is completed")
    public void onFinish(final Player player) {
        final Result result = controller.finishSetup(player);

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_COMPLETED;
                break;
            case DENY:
                path = Path.MESSAGES_ISLAND_INCOMPLETE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("expansions")
    @Description("Shows you the PlaceholderAPI extensions")
    @CommandPermission("island.info")
    public void onExpansion(final Player player) {
        final String format = " &6&l&m*&r &e%bridge_#name#%";

        for (final String expansion : Util.toString(ExpansionType.values())) {
            Util.message(player, format, new String[]{"#name#"}, false, expansion.toLowerCase(Locale.ROOT));
        }
    }

    //TODO: MAKE THIS CONFIGURABLE
    @Subcommand("guide")
    @Description("A Guide for Administrators")
    @CommandPermission("island.info")
    public void onGuide(final Player player) {
        player.sendMessage(Util.colorize("&e&l&m<&6&m------&r &e&lGuide &6&m------&e&l&m>"));
        final String[] guide = {
                "&61. &eHow to create an Island:\n",
                "&61.1. &eYou can create an island by typing &6\"/island create (island-slot)\"\n",

                "\n&62. &eHow to setup an Island:\n",
                "&62.1 &eYou can set &6spawn/point/selection-a/selection-b &elocations types by\n",
                "going to their respective locations and typing &6\"/island set <location-types>\"\n",
                "&eand once you're done with that, type &6\"/island finish\"\n",

                "\n&63. &eHow to join an island:\n",
                "&63.1. &eYou can join an island by \"/join (island-slot)\""};

        player.sendMessage(Util.colorize(Arrays.toString(guide).replace("[" ,"").replace("]", "")));
        player.sendMessage(Util.colorize("&e&l&m<&r&6&m----------------&e&l&m>"));
    }
}
