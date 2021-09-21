package me.tofpu.speedbridge.game.processor.type;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.game.processor.ProcessType;
import org.bukkit.entity.Player;

public abstract class GameItemProcessor {
    public void process(final User user,
            final Player player,
            final ProcessType type) {}
}
