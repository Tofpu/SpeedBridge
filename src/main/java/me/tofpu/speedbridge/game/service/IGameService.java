package me.tofpu.speedbridge.game.service;

import com.sun.istack.internal.NotNull;
import org.bukkit.entity.Player;

public interface IGameService {
    public boolean join(@NotNull final Player player);
    public boolean leave(@NotNull final Player player);
}
