package me.tofpu.speedbridge.game.service.impl;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
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
    public boolean join(@NotNull final Player player){
        final List<Island> islands = islandService.getAvailableIslands();
        if (islands.size() < 1) return false;

        final IIsland island = islands.get(0);
        final IUser user = userService.getOrDefault(player.getUniqueId());

        island.setTakenBy(user);
        player.teleport(island.getLocation());
        // TODO: SEND MESSAGE THAT THEY JOINED!

        return true;
    }

    @Override
    public boolean leave(@NotNull final Player player){
        final IUser user = userService.searchForUUID(player.getUniqueId());
        if (user == null) return false;

        final Island island = this.islandService.getIslandByUser(user);
        island.setTakenBy(null);
        // TODO: TELEPORT PLAYER TO LOBBY!
        // TODO: SEND MESSAGE THAT THEY'VE LEFT!

        return true;
    }
}
