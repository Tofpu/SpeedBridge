package me.tofpu.speedbridge.api.island;

import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.util.Identifier;
import org.bukkit.Location;

import java.util.List;

/**
 * This represents an island, each to their own.
 */
public interface Island extends Identifier {
    /**
     * The island availability
     *
     * @return true if the island is not taken by a player, otherwise false
     */
    boolean isAvailable();

    /**
     * The user whose took the island
     *
     * @return the user that taken the island, otherwise null
     */
    User takenBy();

    // TODO: SHOULD THIS BE THERE? PROBABLY NOT
    /**
     * Setting the new user whose taken the island
     *
     * @param takenBy setting the takenBy user, null if it's available
     */
    void takenBy(final User takenBy);

    /**
     * Island primary location
     *
     * @return the island spawn location
     */
    Location location();

    // TODO: SHOULD THIS BE THERE? NO CLUE
    /**
     * @param location setting the island's spawn location
     */
    void location(final Location location);

    /**
     * The location availability
     *
     * @return true if the island primary location is not null, otherwise false
     */
    boolean hasLocation();

    /**
     * Island's defined slot
     *
     * @return the island slot
     */
    int slot();

    /**
     * The island's defined mode
     *
     * @return the island's mode
     */
    Mode mode();

    /**
     * A list of placed blocks by the user
     *
     * @return a list of placed blocks by the user, will return empty if it's available
     */
    List<Location> placedBlocks();

    /**
     * Island's defined property
     *
     * @return the island properties
     */
    IslandProperties properties();
}
