package me.tofpu.speedbridge.command;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    private final IGameService gameService;

    public CommandManager(@NotNull final IGameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            return false;
        }
        final Player player = (Player) sender;

        if (args.length < 1) return false;
        switch (args[0]){
            case "join":
                // TODO: HAVE A CHECK LATER
                gameService.join(player);
                break;
            case "leave":
                // TODO: HAVE A CHECK LATER
                gameService.leave(player);
                break;
        }
        return false;
    }
}
