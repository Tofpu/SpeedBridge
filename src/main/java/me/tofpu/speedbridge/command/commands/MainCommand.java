package me.tofpu.speedbridge.command.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.RegisteredCommand;
import co.aikar.commands.annotation.*;
import com.google.common.collect.Maps;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

import java.util.Map;

@CommandAlias("speedbridge|game")
public class MainCommand extends BaseCommand {

    private final IUserService userService;
    private final IGameService gameService;
    private final ILobbyService lobbyService;

    public MainCommand(final IUserService userService, final IGameService gameService, final ILobbyService lobbyService) {
        this.userService = userService;
        this.gameService = gameService;
        this.lobbyService = lobbyService;
    }

    @Subcommand("help")
    public void onHelp(final Player player){
        player.sendMessage(Util.colorize("&e&l&m<&6&m------&r &e&lSpeedBridge Commands &6&m------&e&l&m>"));
        for (final Map.Entry<String, RegisteredCommand> test : getSubCommands().entries()){
            player.sendMessage(format(test.getValue()));
        }
        player.sendMessage(Util.colorize("&e&l&m<&r&6&m----------------&e&l&m>&r &e&l&m<&r&6&m----------------&e&l&m>"));
    }

    private String format(final RegisteredCommand<?> command){
        final String format = " &6&l&m*&r &e/%command% &6%syntax% &6&l&m-&r &e%description%";

        final Map<String, String> map = Maps.newHashMap();
        map.put("%command%", command.getCommand());
        map.put("%syntax%", command.getSyntaxText());
        map.put("%description%", command.getHelpText());
        return Util.colorize(Util.WordReplacer.replace(format, map));
    }

    @Subcommand("join")
    public void onJoin(final Player player){
        onJoin(player, null, null);
    }

    @Subcommand("join")
    @Syntax("[slot]")
    @CommandCompletion("@availableIslands")
    public void onJoin(final Player player, @Optional Integer slot){
        onJoin(player, slot, null);
    }

    @Subcommand("join")
    @Syntax("[mode]")
    @CommandCompletion("@modes")
    public void onJoin(final Player player,  @Optional Mode mode){
        onJoin(player, null, mode);
    }

    @Subcommand("leave")
    public void onLeave(final Player player){
        if (!gameService.isPlaying(player)){
            Util.message(player, Path.MESSAGES_NOT_PLAYING);
            return;
        }
        gameService.leave(player);
    }

    @Subcommand("score")
    public void onScore(final Player player){
        final Map<String, Double> map = Maps.newHashMap();
        final IUser user = userService.searchForUUID(player.getUniqueId());
        final UserProperties properties = user == null ? null : user.getProperties();

        map.put("%score%", user == null ? 0 : properties.getTimer() == null ? 0 : properties.getTimer().getResult());
        Util.message(player, Path.MESSAGES_YOUR_SCORE, map);
    }

    @Subcommand("leaderboard")
    public void onLeaderboard(final Player player){
        player.sendMessage(lobbyService.getLeaderboard().printLeaderboard());
    }

    @Subcommand("lobby")
    public void onLobby(final Player player){
        if (gameService.isPlaying(player)) {
            gameService.leave(player);
        }
        if (lobbyService.hasLobbyLocation()) player.teleport(lobbyService.getLobbyLocation());
    }

    private void onJoin(final Player player, final Integer integer, final Mode mode){
        final Result result;

        if (integer != null) {
          result = gameService.join(player, integer);
        } else if (mode != null){
            result = gameService.join(player, mode);
        } else result = gameService.join(player);

        switch (result) {
            case DENY:
                Util.message(player, Path.MESSAGES_ALREADY_JOINED);
                break;
            case FULL:
                Util.message(player, Path.MESSAGES_NOT_AVAILABLE);
                break;
            case SUCCESS:
                Util.message(player, Path.MESSAGES_JOINED);
                break;
            case INVALID_LOBBY:
                if (player.isOp()) {
                    Util.message(player, Path.MESSAGES_NO_LOBBY);
                    break;
                } else Util.message(player, Path.MESSAGES_NOT_AVAILABLE);
        }
    }
}
