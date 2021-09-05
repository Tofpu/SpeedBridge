package me.tofpu.speedbridge.game.listener.machanic;

import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final UserService userService;
    private final IslandService islandService;
    private final GameService gameService;

    public BlockPlaceListener(UserService userService, IslandService islandService, GameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (!gameService.isPlaying(player)) return;

        final User user = userService.searchForUUID(player.getUniqueId());
        final Island island = islandService.getIslandBySlot(user.properties().islandSlot());

        final Location location = event.getBlockPlaced().getLocation();

        final TwoSection twoSection = (TwoSection) island.properties().get("selection");
        if (!Cuboid.of(twoSection.pointA(), twoSection.pointB()).isIn(location)) {
            event.setCancelled(true);
            return;
        }

        if (!gameService.hasTimer(user)) {
            gameService.addTimer(user);
        }

        island.placedBlocks().add(event.getBlockPlaced().getLocation());
        event.getItemInHand().setAmount(64);
    }
}
