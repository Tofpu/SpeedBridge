package me.tofpu.speedbridge.game.service;

import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.timer.Timer;
import org.bukkit.entity.Player;

public interface IGameService {
    Result join(final Player player);

    Result join(final Player player, final int slot);

    Result join(final Player player, final Mode mode);

    Result join(final IUser user, final IIsland island);

    Result leave(final Player player);

    boolean isPlaying(final Player player);

    void addTimer(final IUser user);

    boolean hasTimer(final IUser user);

    Timer getTimer(final IUser user);

    void updateTimer(final IUser user);

    void resetTimer(final IUser user);

    void reset(final IUser user);
}
