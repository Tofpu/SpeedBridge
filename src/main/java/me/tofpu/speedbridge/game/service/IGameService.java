package me.tofpu.speedbridge.game.service;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.user.timer.Timer;
import org.bukkit.entity.Player;

public interface IGameService {
    public Result join(@NotNull final Player player);

    public Result leave(@NotNull final Player player);

    public void updateTimer(@NotNull final Player player, @NotNull final Timer timer);
}
