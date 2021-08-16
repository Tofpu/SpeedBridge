package me.tofpu.speedbridge.data.listener;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.service.UserService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final UserService userService;
    private final IslandService islandService;
    private final GameService gameService;
    private final DataManager dataManager;

    public PlayerQuitListener(UserService userService, IslandService islandService, GameService gameService, DataManager dataManager) {
        this.userService = userService;
        this.islandService = islandService;
        this.gameService = gameService;
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (gameService.isPlaying(player)) {
            final User user = userService.searchForUUID(player.getUniqueId());

            islandService.resetIsland(user.getProperties().getIslandSlot());
            gameService.leave(player);
        }

        dataManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
