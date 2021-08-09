package me.tofpu.speedbridge.data.listener;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final IUserService userService;
    private final IIslandService islandService;
    private final IGameService gameService;
    private final DataManager dataManager;

    public PlayerQuitListener(IUserService userService, IIslandService islandService, IGameService gameService, DataManager dataManager) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (gameService.isPlaying(player)) {
            final IUser user = userService.searchForUUID(player.getUniqueId());

            islandService.resetIsland(user.getProperties().getIslandSlot());
            gameService.leave(player);
        }

        dataManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
