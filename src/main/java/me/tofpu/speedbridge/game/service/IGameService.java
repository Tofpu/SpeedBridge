package me.tofpu.speedbridge.game.service;

import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.entity.Player;

public interface IGameService {
    public Result join(final Player player);

    public Result join(final Player player, final int slot);

    public Result join(final Player player, final Mode mode);

    public Result join(final IUser user, final IIsland island);

    public Result leave(final Player player);

    public boolean isPlaying(final Player player);

    public void addTimer(final IUser user);

    public boolean hasTimer(final IUser user);

    public void updateTimer(final IUser user);

    public void resetTimer(final IUser user);

    public void reset(final IUser user);
}
