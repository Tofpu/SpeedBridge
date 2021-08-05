package me.tofpu.speedbridge.listener;

import me.tofpu.speedbridge.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final DataManager dataManager;

    public PlayerJoinListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(final PlayerJoinEvent event) {
        dataManager.loadUser(event.getPlayer().getUniqueId());
    }
}
