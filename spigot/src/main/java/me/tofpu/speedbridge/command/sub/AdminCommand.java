package me.tofpu.speedbridge.command.sub;

import co.aikar.commands.annotation.*;
import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.game.Result;
import me.tofpu.speedbridge.api.game.SetupStage;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.command.BridgeBaseCommand;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.expansion.ExpansionType;
import me.tofpu.speedbridge.model.object.game.controller.GameController;
import me.tofpu.speedbridge.model.service.IslandServiceImpl;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

@CommandAlias("island")
public class AdminCommand extends BridgeBaseCommand {
    private final SpeedBridge plugin;

    private final IslandServiceImpl islandService;
    private final LobbyService lobbyService;
    private final GameService gameService;

    private final GameController gameController;
    private final DataManager dataManager;

    public AdminCommand(SpeedBridge plugin, final IslandServiceImpl islandService, final LobbyService lobbyService, final GameService gameService, final GameController gameController, final DataManager dataManager) {
        super("island");
        this.plugin = plugin;
        this.islandService = islandService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        this.gameController = gameController;
        this.dataManager = dataManager;
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

        final Path.Value<?> path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_CREATION;
                break;
            case FAIL:
                path = Path.MESSAGES_ISLAND_EXISTS;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("remove")
    @CommandPermission("island.remove")
    @Syntax("<slot>")
    @Description("Removes an island")
    public void onDelete(final Player player, int slot) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = islandService.removeIsland(slot);

        final Path.Value<?> path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_REMOVAL;
                break;
            case FAIL:
                path = Path.messages_ISLAND_REMOVAL_FAIL;
                break;
            case INVALID_ISLAND:
                path = Path.MESSAGES_INVALID_ISLAND;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path, new String[]{"%slot%"}, slot + "");
    }

    @Subcommand("revert")
    @CommandPermission("island.revert")
    @Syntax("")
    @Description("Reverts the removal of an island")
    public void onRevert(final Player player) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Island island = islandService.revert();

        final Path.Value<?> path;
        if (island == null) {
            path = Path.MESSAGES_ISLAND_REVERT_FAIL;
        } else {
            path = Path.MESSAGES_ISLAND_REVERT;
            islandService.addIsland(island);
        }

        if (island != null) {
            Util.message(player, path, new String[]{"%slot%"}, island.identifier());
        } else {
            Util.message(player, path);
        }
    }

    @Subcommand("set")
    @CommandPermission("island.set")
    @CommandCompletion("@setupStage")
    @Syntax("<location-type>")
    @Description("Sets the current location point")
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

        final Path.Value<?> path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_LOBBY_LOCATION;
                break;
            case FAIL:
                path = Path.MESSAGES_INVALID_ISLAND;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("finish")
    @CommandPermission("island.finish")
    @Description("Completes your current island setup")
    public void onFinish(final Player player) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.finishSetup(player);

        final Path.Value<?> path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_COMPLETED;
                break;
            case FAIL:
                path = Path.MESSAGES_ISLAND_INCOMPLETE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("modify")
    @Syntax("<slot>")
    @Description("Modify an island location points")
    @CommandPermission("island.modify")
    public void onModify(final Player player, final int slot) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.modifyIsland(player, slot);

        final Path.Value<?> path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_ISLAND_MODIFICATION;
                break;
            case FAIL:
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
    @Description("Cancels your current island setup")
    @CommandPermission("island.modify")
    public void onCancel(final Player player) {
        if (gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_CANNOT_EDIT);
            return;
        }
        final Result result = gameController.cancelSetup(player);

        final Path.Value<?> path;
        switch (result) {
            case SUCCESS:
                path = Path.MESSAGES_CANCEL_SETUP;
                if (lobbyService.hasLobbyLocation())
                    player.teleport(lobbyService.getLobbyLocation());
                break;
            case FAIL:
                path = Path.MESSAGES_NO_SETUP;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }

    @Subcommand("reload")
    @Description("Applies the new changes")
    @CommandPermission("island.reload")
    public void onReload(final CommandSender sender) {
        dataManager.reload();
        Util.message(sender, Path.MESSAGES_RELOADED);
    }

    @Subcommand("expansions")
    @Description("Lists the PlaceholderAPI extensions")
    @CommandPermission("island.info")
    public void onExpansion(final CommandSender sender) {
        final String format = " &6&l&m*&r &e%bridge_#name#% &6- &e#description#";

        for (final ExpansionType expansion : ExpansionType.values()) {
            Util.message(sender, format, new String[]{"#name#", "#description#"}, false, expansion.name().toLowerCase(Locale.ROOT).replace("_", "-"), expansion.description());
        }
    }
}