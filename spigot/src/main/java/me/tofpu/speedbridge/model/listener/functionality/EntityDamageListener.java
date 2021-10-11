package me.tofpu.speedbridge.model.listener.functionality;

import me.tofpu.speedbridge.api.model.service.GameService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final GameService gameService;

    public EntityDamageListener(final GameService gameService) {
        this.gameService = gameService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();

        // if the player is playing, cancel entity damage
        if (gameService.isPlaying(player) || gameService.isSpectating(player)) event.setCancelled(true);
    }
}
