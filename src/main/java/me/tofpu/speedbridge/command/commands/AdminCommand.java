package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.annotation.*;
import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.command.commands.base.BridgeBaseCommand;
import me.tofpu.speedbridge.data.file.config.Config;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.expansion.type.ExpansionType;
import me.tofpu.speedbridge.game.controller.GameController;
import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

@CommandAlias("island")
public class AdminCommand extends BridgeBaseCommand {
    private final SpeedBridge plugin;

    private final LobbyService lobbyService;
    private final GameService gameService;

    private final GameController gameController;

    public AdminCommand(SpeedBridge plugin, final LobbyService lobbyService, final GameService gameService, final GameController gameController) {
        super("island");
        this.plugin = plugin;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.gameController = gameController;
    }

    @Override
    @Private
    @Subcommand("help")
    @CommandPermission("island.info")
    @Description("Shows you all the available commands")
    public void onHelp(CommandSender sender) {
        super.onHelp(sender);
    }

    @Override
    @Private
    @CatchUnknown
    @CommandPermission("island.info")
    public void onUnknownCommand(CommandSender sender) {
        super.onUnknownCommand(sender);
    }

    @Subcommand("create")
    @CommandPermission("island.create")
    @Syntax("<slot>")
    @Description("Creates an island in that particular slot")
    public void onCreate(final Player player, int slot) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.createIsland(player, slot);

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
    @Syntax("<location-type>")
    @Description("Set the island locations")
    public void onSet(final Player player, final String arg) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }

        final SetupStage stage;
        if ((stage = SetupStage.getMatch(arg)) == null) {
            Util.message(player, Path.MESSAGES_INVALID_TYPE);
            return;
        }
        if (stage == SetupStage.LOBBY) {
            lobbyService.setLobbyLocation(player.getLocation());
            Util.message(player, Path.MESSAGES_LOBBY_LOCATION);
            return;
        }
        final Result result = gameController.setupIsland(player, stage);

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_LOBBY_LOCATION;
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
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.finishSetup(player);

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_COMPLETED;
                break;
            case DENY:
                path = Path.MESSAGES_ISLAND_INCOMPLETE;
                break;
            case INVALID_LOBBY:
                path = Path.MESSAGES_NO_COMPLETE;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("modify")
    @Syntax("<slot>")
    @Description("Allows you to modify an islands' locations")
    @CommandPermission("island.modify")
    public void onModify(final Player player, final int slot) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.modifyIsland(player, slot);

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_MODIFICATION;
                break;
            case DENY:
                path = Path.MESSAGES_INVALID_ISLAND;
                break;
            case FULL:
                path = Path.MESSAGES_SINGLE_EDIT_ONLY;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("cancel")
    @Description("Cancels the current island setup")
    @CommandPermission("island.modify")
    public void onCancel(final Player player) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.cancelSetup(player);

        final Path path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_CANCEL_SETUP;
                break;
            case DENY:
                path = Path.MESSAGES_NO_SETUP;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("reload")
    @Description("Apply new changes to settings & messages file")
    @CommandPermission("island.reload")
    public void onReload(final CommandSender sender) {
        Config.reload(plugin);
        Util.message(sender, Path.MESSAGES_RELOADED);
    }

    @Subcommand("expansions")
    @Description("Shows you the PlaceholderAPI extensions")
    @CommandPermission("island.info")
    public void onExpansion(final CommandSender sender) {
        final String format = " &6&l&m*&r &e%bridge_#name#%";

        for (final String expansion : Util.toString(ExpansionType.values())) {
            Util.message(sender, format, new String[]{"#name#"}, false, expansion.toLowerCase(Locale.ROOT));
        }
    }

    //TODO: MAKE THIS CONFIGURABLE
    @Subcommand("guide")
    @Description("A Guide for Administrators")
    @CommandPermission("island.info")
    public void onGuide(final CommandSender sender) {
        Util.message(sender, Path.MESSAGES_GUIDE);
    }
}