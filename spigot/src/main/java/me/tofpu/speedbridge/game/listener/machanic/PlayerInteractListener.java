package me.tofpu.speedbridge.game.listener.machanic;

import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.island.point.Point;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        // if the event action is not physical (e.g: pressure-plate)
        if (event.getAction() != Action.PHYSICAL) return;
        final Player player = event.getPlayer();

        // if the player is not not playing
        if (!gameService.isPlaying(player)) return;
        final User user = userService.searchForUUID(player.getUniqueId());
        final Island island = islandService.getIslandBySlot(user.properties().islandSlot());

        final Point section = island.properties().get("endpoint");
        final Location pressurePlate = event.getClickedBlock().getLocation();

        // if the timer has started and the pressure plate location equals to the island point
        if (gameService.hasTimer(user) && Util.isEqual(pressurePlate, section.pointA())) {
            gameService.updateTimer(user);
        }
    }
}
