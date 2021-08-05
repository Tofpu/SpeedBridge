package me.tofpu.speedbridge.listener;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final IUserService userService;
    private final IIslandService islandService;
    private final IGameService gameService;

    public PlayerMoveListener(@NotNull final IUserService userService, @NotNull final IIslandService islandService, @NotNull final IGameService gameService) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
    }

    @EventHandler
    private void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final IUser user;

        if ((user = userService.searchForUUID(player.getUniqueId())) == null) return;
        final Location fromLocation = event.getFrom();
        final IIsland island = islandService.getIslandBySlot(user.getProperties().getIslandSlot());
        final IslandProperties properties = island.getProperties();

        if (isSame(fromLocation, properties.getLocationA())) {
            gameService.addTimer(user);
        } else if (gameService.hasTimer(user) && isSame(fromLocation, properties.getLocationB())) {
            gameService.updateTimer(player);
        }
    }

    private boolean isSame(final Location from, final Location to) {
        from.add(0.5, 0, 0.5);
        to.add(0.5, 0, 0.5);

        return from.equals(to);
    }
}
