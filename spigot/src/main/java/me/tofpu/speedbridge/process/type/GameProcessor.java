package me.tofpu.speedbridge.process.type;

import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.model.handler.GameHandler;
import me.tofpu.speedbridge.process.ProcessType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class GameProcessor {
    public void process(final GameHandler gameHandler,
            final User user,
            final ProcessType type) {
        process(gameHandler, user, user.player(), type);
    }

    public void process(final GameHandler gameHandler,
            final User user,
            final Player player,
            final ProcessType type) {}

    public void process(final GameHandler gameHandler,
            final Island island, final User user,
            final ProcessType type) {
        process(gameHandler, island, user, user.player(), type);
    }

    public void process(final GameHandler gameHandler,
            final Island island, final User user,
            final Player player,
            final ProcessType type) {}

    public void process(final GameHandler gameHandler,
            final Location location,
            final ProcessType type,
            final User... users) {}
}
