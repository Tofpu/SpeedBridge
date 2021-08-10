package me.tofpu.speedbridge.island.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.user.IUser;

import java.io.File;
import java.util.List;

public interface IIslandService {
    void addIsland(final IIsland island);

    void removeIsland(final IIsland island);

    IIsland getIslandBySlot(final int slot);

    IIsland getIslandByUser(final IUser takenBy);

    List<IIsland> getAvailableIslands();

    List<IIsland> getAvailableIslands(final Mode mode);

    void resetBlocks(final IIsland island);

    void resetIsland(final int slot);

    void saveAll(final Gson gson, final File directory);

    void loadAll(final Gson gson, final File directory);
}
