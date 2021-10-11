package me.tofpu.speedbridge.process.type;

import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.model.service.GameServiceImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class GameProcessor {
    public void process(final GameServiceImpl gameService,
            final User user,
            final Player player,
            final ProcessType type) {}

    public void process(final GameServiceImpl gameService,
            final Island island, final User user,
            final Player player,
            final ProcessType type) {}

    public void process(final GameServiceImpl gameService,
            final Location location,
            final ProcessType type,
            final User... users) {}
}
