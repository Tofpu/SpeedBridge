package me.tofpu.speedbridge.model.listener.machanic;

import me.tofpu.speedbridge.api.model.service.GameService;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.service.IslandService;
import me.tofpu.speedbridge.api.model.object.island.point.Point;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.service.UserService;
import me.tofpu.speedbridge.util.Util;
import me.tofpu.speedbridge.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final UserService userService;
    private final IslandService islandService;
    private final GameService gameService;

    public PlayerInteractListener(final UserService userService, final IslandService islandService, final GameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        // if the player is not playing

        boolean spectating = gameService.isSpectating(player);
        if (!gameService.isPlaying(player) && !spectating) return;

        if (spectating) event.setCancelled(true);

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
                // if the player haven't interacted with an item, or the item
                // material doesn't equal to red dye or the player isn't
                // spectating
                if (!event.hasItem() || event.getMaterial() != XMaterial.RED_DYE
                        .parseMaterial() || !spectating)
                    return;
                gameService.leave(player);
                break;
            case PHYSICAL:
                // if the player is not not playing
                final User user = userService.find(player.getUniqueId());
                final Island island = islandService.getIslandBySlot(user.properties()
                        .islandSlot());

                final Point section = island.properties().get("endpoint");
                final Location pressurePlate = event.getClickedBlock().getLocation();

                // if the timer has started and the pressure plate location equals to the island point
                if (gameService.hasTimer(user) && Util.isEqual(pressurePlate, section
                        .pointA())) {
                    gameService.updateTimer(user);
                }
                break;
        }
    }
}
