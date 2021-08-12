package me.tofpu.speedbridge.game.listener.functionality;

import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final IUserService userService;
    private final IIslandService islandService;
    private final IGameService gameService;

    public BlockPlaceListener(IUserService userService, IIslandService islandService, IGameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (!gameService.isPlaying(player)) return;

        final IUser user = userService.searchForUUID(player.getUniqueId());
        final IIsland island = islandService.getIslandBySlot(user.getProperties().getIslandSlot());

        final Location location = event.getBlockPlaced().getLocation();

        final TwoSection twoSection = island.getProperties().get("selection");
        if (!Cuboid.of(twoSection.getSectionA(), twoSection.getSectionB()).isIn(location)) {
            event.setCancelled(true);
            return;
        }

        island.getPlacedBlocks().add(event.getBlockPlaced().getLocation());
        event.getItemInHand().setAmount(64);
    }
}
