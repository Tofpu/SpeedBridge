package me.tofpu.speedbridge.api.model.service;

import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;

import java.util.List;
import java.util.Optional;

/**
 * This handles everything that is island related.
 * @see Island
 */
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
     * @param slot the island slot you want removed
     */
    Result removeIsland(final int slot);

    Optional<Island> findIslandBy(final IslandRepository.SearchAlgorithm algorithm);
}
