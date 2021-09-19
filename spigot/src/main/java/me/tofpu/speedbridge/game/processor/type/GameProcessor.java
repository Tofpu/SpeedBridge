package me.tofpu.speedbridge.game.processor.type;

import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.game.processor.ProcessType;
import me.tofpu.speedbridge.game.service.GameServiceImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class GameProcessor {
    public void process(final User user, final ProcessType type) {
        process(user, user.player(), type);
    }

    public void process(final User user, final Player player,
            final ProcessType type) {}

    public void process(final GameServiceImpl gameService, final User user,
            final Player player, Island island,
     final ProcessType type) {}

    public void process(final GameServiceImpl gameService,
            final Location location,
            final ProcessType type,
            final User... users) {}
}
