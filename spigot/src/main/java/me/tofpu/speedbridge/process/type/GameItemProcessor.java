package me.tofpu.speedbridge.process.type;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.process.ProcessType;
import org.bukkit.entity.Player;

public abstract class GameItemProcessor {
    public void process(final User user,
            final Player player,
            final ProcessType type) {}
}
