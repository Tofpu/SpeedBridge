package me.tofpu.speedbridge.listener;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final ILobbyService lobbyService;
    private final DataManager dataManager;

    public PlayerJoinListener(final ILobbyService lobbyService, final DataManager dataManager) {
        this.lobbyService = lobbyService;
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (lobbyService.hasLobbyLocation()) player.teleport(lobbyService.getLobbyLocation());
        else if (player.isOp()) {
            //TODO: SEND MESSAGE SAYING THEY HAVE TO SET A LOBBY!
        }

        dataManager.loadUser(event.getPlayer().getUniqueId());
    }
}
