package me.tofpu.speedbridge.game.listener.functionality;

import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.service.UserService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {
    private final UserService userService;
    private final IslandService islandService;
    private final GameService gameService;

    public BlockBreakListener(UserService userService, IslandService islandService, GameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Location blockLocation = event.getBlock().getLocation();

        if (!gameService.isPlaying(player)) return;
        final User user = userService.searchForUUID(player.getUniqueId());
        final Island island = islandService.getIslandBySlot(user.properties().islandSlot());

        final List<Location> locations = island.placedBlocks();
        if (!locations.contains(blockLocation)) {
            event.setCancelled(true);
            return;
        }

        locations.remove(blockLocation);
    }
}
