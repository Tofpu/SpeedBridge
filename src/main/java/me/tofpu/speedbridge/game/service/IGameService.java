package me.tofpu.speedbridge.game.service;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.entity.Player;

public interface IGameService {
    public Result join(@NotNull final Player player);

    public Result leave(@NotNull final Player player);

    public void addTimer(@NotNull final IUser user);

    public boolean hasTimer(@NotNull final IUser user);

    public void updateTimer(@NotNull final Player player);
}
