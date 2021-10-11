package me.tofpu.speedbridge.model.listener.general;

import me.tofpu.speedbridge.api.model.service.GameService;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.service.UserService;
import me.tofpu.speedbridge.data.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final UserService userService;
    private final GameService gameService;
    private final DataManager dataManager;

    public PlayerQuitListener(final UserService userService, final GameService gameService, final DataManager dataManager) {
        this.userService = userService;
        this.gameService = gameService;
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final boolean playing = gameService.isPlaying(player);

        if (playing || gameService.isSpectating(player)) {
            final User user = userService.find(player.getUniqueId());

            if (playing) gameService.resetIsland(user.properties().islandSlot());
            gameService.leave(player);
        }

        dataManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
