package me.tofpu.speedbridge.game.listener.machanic;

import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.service.UserService;
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
        if (event.getAction() != Action.PHYSICAL) return;
        final Player player = event.getPlayer();

        if (!gameService.isPlaying(player)) return;
        final User user = userService.searchForUUID(player.getUniqueId());
        final Island island = islandService.getIslandBySlot(user.getProperties().getIslandSlot());

        final Point section = island.getProperties().get("point");
        final Location pressurePlate = event.getClickedBlock().getLocation();
//        if (isEqual(pressurePlate, section.getPointA())) {
//            player.sendMessage("Pressed on point-a");
//            gameService.resetTimer(user);
//            gameService.addTimer(user);
//        } else

        if (gameService.hasTimer(user) && Util.isEqual(pressurePlate, section.getPointA())) {
            gameService.updateTimer(user);
        }
    }
}
