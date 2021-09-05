package me.tofpu.speedbridge.api.game;

import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.timer.Timer;
import org.bukkit.entity.Player;

public interface GameService {
    Result join(final Player player);

    Result join(final Player player, final int slot);

    Result join(final Player player, final Mode mode);

    Result join(final User user, final Island island);

    Result leave(final Player player);

    boolean isPlaying(final Player player);

    void addTimer(final User user);

    boolean hasTimer(final User user);

    Timer getTimer(final User user);

    void updateTimer(final User user);

    void resetTimer(final User user);

    void reset(final User user);

    void resetBlocks(final Island island);

    void resetIsland(final int slot);
}
