package me.tofpu.speedbridge.listener;

import me.tofpu.speedbridge.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final DataManager dataManager;

    public PlayerQuitListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        dataManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
