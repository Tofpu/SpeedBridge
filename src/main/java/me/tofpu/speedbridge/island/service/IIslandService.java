package me.tofpu.speedbridge.island.service;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.user.IUser;

import java.util.List;

public interface IIslandService {
    public void addIsland(@NotNull final IIsland island);

    public void removeIsland(@NotNull final IIsland island);

    @Nullable
    public IIsland getIslandBySlot(@NotNull final int slot);

    @Nullable
    public IIsland getIslandByUser(@NotNull final IUser takenBy);

    @NotNull
    public List<IIsland> getAvailableIslands();
}
