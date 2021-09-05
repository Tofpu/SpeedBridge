package me.tofpu.speedbridge.api.island;

import me.tofpu.speedbridge.api.user.User;

import java.io.File;
import java.util.List;

public interface IslandService {
   void addIsland(final Island island);

    void removeIsland(final Island island);

    Island getIslandBySlot(final int slot);

    Island getIslandByUser(final User takenBy);

    List<Island> getAvailableIslands();

    List<Island> getAvailableIslands(final Mode mode);

    void saveAll(final File directory, final boolean emptyList);

    void loadAll(final File directory);
}
