package me.tofpu.speedbridge.game.listener.machanic;

import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final IUserService userService;
    private final IIslandService islandService;
    private final IGameService gameService;

    public PlayerInteractListener(final IUserService userService, final IIslandService islandService, final IGameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler
    private void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;
        final Player player = event.getPlayer();

        if (!gameService.isPlaying(player)) return;
        final IUser user = userService.searchForUUID(player.getUniqueId());
        final IIsland island = islandService.getIslandBySlot(user.getProperties().getIslandSlot());

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
