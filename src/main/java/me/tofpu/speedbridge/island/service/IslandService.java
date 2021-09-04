package me.tofpu.speedbridge.island.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.user.User;

import java.io.File;
import java.util.List;

public interface IslandService {
    void addIsland(final Island island);

    void removeIsland(final Island island);

    Island getIslandBySlot(final int slot);

    Island getIslandByUser(final User takenBy);

    List<Island> getAvailableIslands();

    List<Island> getAvailableIslands(final Mode mode);

    void resetBlocks(final Island island);

    void resetIsland(final int slot);

    void saveAll(final Gson gson, final File directory);

    void loadAll(final Gson gson, final File directory);
}
