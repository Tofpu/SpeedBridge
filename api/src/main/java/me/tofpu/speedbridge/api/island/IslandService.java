package me.tofpu.speedbridge.api.island;

import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;

import java.io.File;
import java.util.List;

public interface IslandService {
    /**
     * Adds this island instance to the island's list
     *
     * @param island the island instance you want added
     */
    void addIsland(final Island island);

    /**
     * Removes tis island instance from the island's list
     *
     * @param island the island instance you want removed
     */
    void removeIsland(final Island island);

    /**
     * Looks up the loaded islands associated with this slot
     *
     * @param slot island slot
     *
     * @return the island instance associated with this slot
     */
    Island getIslandBySlot(final int slot);

    /**
     * Looks up the loaded islands associated with this user
     *
     * @param user the user instance you want looked up
     *
     * @return the island instance associated with this user
     */
    Island getIslandByUser(final User user);

    /**
     * A list of available islands
     *
     * @return a list of available islands
     */
    List<Island> getAvailableIslands();

    /**
     * A list of available islands associated with this mode
     *
     * @param mode the island's mode type
     *
     * @return a list of available islands associated with this mode
     */
    List<Island> getAvailableIslands(final Mode mode);

    // TODO: Should this be here, hmm...
    void saveAll(final File directory, final boolean emptyList);

    // TODO: Should this be here, hmm...
    void loadAll(final File directory);
}
