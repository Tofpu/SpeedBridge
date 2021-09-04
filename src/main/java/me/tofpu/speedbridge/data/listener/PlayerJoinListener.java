package me.tofpu.speedbridge.data.listener;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final LobbyService lobbyService;
    private final DataManager dataManager;

    public PlayerJoinListener(final LobbyService lobbyService, final DataManager dataManager) {
        this.lobbyService = lobbyService;
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (lobbyService.hasLobbyLocation()) player.teleport(lobbyService.getLobbyLocation());
        else if (player.isOp()) {
            Util.message(player, Path.MESSAGES_NO_LOBBY);
        }

        lobbyService.getLeaderboard().check(dataManager.loadUser(event.getPlayer().getUniqueId()));
    }
}
