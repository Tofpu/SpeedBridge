package me.tofpu.speedbridge.game.service.impl;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.user.timer.Timer;
import org.bukkit.entity.Player;

import java.util.List;

// LEADERBOARD?
//
public class GameService implements IGameService {
    private final IIslandService islandService;
    private final IUserService userService;

    public GameService(IIslandService islandService, IUserService userService) {
        this.islandService = islandService;
        this.userService = userService;
    }

    @Override
    public Result join(@NotNull final Player player) {
        final IUser user = userService.getOrDefault(player.getUniqueId());
        if (user != null && user.getProperties().getIslandSlot() != null) return Result.DENY;

        final List<IIsland> islands = islandService.getAvailableIslands();
        if (islands.size() < 1) return Result.FULL;

        final IIsland island = islands.get(0);

        user.getProperties().setIslandSlot(island.getSlot());
        island.setTakenBy(user);
        player.teleport(island.getLocation());
        // TODO: SEND MESSAGE THAT THEY JOINED!

        return Result.SUCCESS;
    }

    @Override
    public Result leave(@NotNull final Player player) {
        final IUser user = userService.searchForUUID(player.getUniqueId());
        if (user == null) return Result.DENY;

        user.getProperties().setIslandSlot(null);
        final IIsland island = this.islandService.getIslandByUser(user);
        island.setTakenBy(null);
        // TODO: TELEPORT PLAYER TO LOBBY!
        // TODO: SEND MESSAGE THAT THEY'VE LEFT!

        return Result.SUCCESS;
    }

    @Override
    public void updateTimer(@NotNull final Player player, @NotNull final Timer timer) {
        final IUser user = userService.searchForUUID(player.getUniqueId());
        if (user == null) {
            return;
        }

        final UserProperties properties = user.getProperties();
        if (properties.getTimer().getResult() <= timer.getResult()) {
            // TODO: SEND MESSAGE THAT THEY HAVEN'T BEATEN THEIR LOWEST RECORD.

        } else {
            // TODO: SEND MESSAGE THAT THEY HAVE BEATEN THEIR LOWEST RECORD.
            properties.setTimer(timer);
        }
        // TODO: SEND MESSAGE MAYBE?
        player.teleport(islandService.getIslandBySlot(user.getProperties().getIslandSlot()).getLocation());
    }
}
