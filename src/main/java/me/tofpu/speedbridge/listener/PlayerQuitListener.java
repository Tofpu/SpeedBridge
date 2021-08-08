package me.tofpu.speedbridge.listener;

import me.tofpu.speedbridge.data.DataManager;
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
    private final DataManager dataManager;

    public PlayerQuitListener(IUserService userService, IIslandService islandService, DataManager dataManager) {
        this.userService = userService;
        this.islandService = islandService;
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final IUser user;
        if ((user = userService.searchForUUID(player.getUniqueId())) != null) {
            islandService.resetIsland(user.getProperties().getIslandSlot());
        }

        dataManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
