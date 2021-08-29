package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.annotation.*;
import me.tofpu.speedbridge.command.commands.base.BridgeBaseCommand;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("speedbridge|game")
public class MainCommand extends BridgeBaseCommand {

    private final UserService userService;
    private final GameService gameService;
    private final LobbyService lobbyService;

    public MainCommand(final UserService userService, final GameService gameService, final LobbyService lobbyService) {
        super("game");
        this.userService = userService;
        this.gameService = gameService;
        this.lobbyService = lobbyService;
    }

    @Override
    @Private
    @Subcommand("help")
    @Description("Shows you all the available commands")
    public void onHelp(final CommandSender sender) {
        super.onHelp(sender);
    }

    @Override
    @Private
    @CatchUnknown
    public void onUnknownCommand(final CommandSender sender) {
        super.onUnknownCommand(sender);
    }

    @Subcommand("join")
    @CommandAlias("join")
    @Syntax("[mode]|[slot]")
    @CommandCompletion("@modes|@availableIslands")
    @Description("To get started practicing")
    public void onJoin(final Player player, @Optional String arg) {
        Integer integer = Util.parseInt(arg);
        Mode mode = null;
        if (integer == null) {
            mode = ModeManager.getModeManager().get(arg);
        }
        onJoin(player, integer, mode);
    }

    @Subcommand("leave")
    @CommandAlias("leave")
    @Description("To leave the practicing island")
    public void onLeave(final Player player) {
        if (!gameService.isPlaying(player)) {
            Util.message(player, Path.MESSAGES_NOT_PLAYING);
            return;
        }
        gameService.leave(player);
    }

    @Subcommand("leaderboard")
    @CommandAlias("leaderboard")
    @Description("Shows you the top 10 best performers")
    public void onLeaderboard(final CommandSender player) {
        player.sendMessage(lobbyService.getLeaderboard().printLeaderboard());
    }

    @Subcommand("score")
    @CommandAlias("score")
    @Description("Your personal best score")
    public void onScore(final Player player) {
        final User user = userService.searchForUUID(player.getUniqueId());
        final UserProperties properties = user == null ? null : user.getProperties();

        double score = user == null ? 0 : properties.getTimer() == null ? 0 : properties.getTimer().getResult();
        Util.message(player, Path.MESSAGES_YOUR_SCORE, new String[]{"%score%"}, score + "");
    }

    @Subcommand("lobby")
    @CommandAlias("lobby")
    @Description("Teleports you back to the Lobby")
    public void onLobby(final Player player) {
        if (gameService.isPlaying(player)) {
            gameService.leave(player);
        }
        if (lobbyService.hasLobbyLocation()) player.teleport(lobbyService.getLobbyLocation());
    }

    private void onJoin(final Player player, final Integer integer, final Mode mode) {
        final Result result;
        if (integer != null) {
            result = gameService.join(player, integer);
        } else if (mode != null) {
            result = gameService.join(player, mode);
        } else result = gameService.join(player);

        final Path path;
        switch (result) {
            case DENY:
                path = Path.MESSAGES_ALREADY_JOINED;
                break;
            case FULL:
            case INVALID_ISLAND:
                path = Path.MESSAGES_NOT_AVAILABLE;
                break;
            case NONE:
                path = Path.MESSAGES_NO_AVAILABLE;
                break;
            case SUCCESS:
                path = Path.MESSAGES_JOINED;
                break;
            case INVALID_LOBBY:
                if (player.isOp()) {
                    path = Path.MESSAGES_NO_LOBBY;
                    break;
                } else path = Path.MESSAGES_NO_AVAILABLE;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

        Util.message(player, path);
    }
}
