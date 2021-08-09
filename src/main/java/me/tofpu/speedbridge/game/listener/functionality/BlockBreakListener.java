package me.tofpu.speedbridge.game.listener.functionality;

import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {
    private final IUserService userService;
    private final IIslandService islandService;
    private final IGameService gameService;

    public BlockBreakListener(IUserService userService, IIslandService islandService, IGameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Location blockLocation = event.getBlock().getLocation();

        if (!gameService.isPlaying(player)) return;
        final IUser user = userService.searchForUUID(player.getUniqueId());
        final IIsland island = islandService.getIslandBySlot(user.getProperties().getIslandSlot());

        final List<Location> locations = island.getPlacedBlocks();
        if (!locations.contains(blockLocation)) {
            event.setCancelled(true);
            return;
        }

        locations.remove(blockLocation);
    }
}
