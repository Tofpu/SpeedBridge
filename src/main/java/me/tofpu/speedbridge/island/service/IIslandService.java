package me.tofpu.speedbridge.island.service;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.user.IUser;

import java.util.List;

public interface IIslandService {
    public void addIsland(@NotNull final Island island);

    public void removeIsland(@NotNull final Island island);

    @Nullable
    public Island getIslandBySlot(@NotNull final int slot);

    @Nullable
    public Island getIslandByUser(@NotNull final IUser takenBy);

    @NotNull
    public List<Island> getAvailableIslands();
}
