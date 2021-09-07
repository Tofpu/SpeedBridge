package me.tofpu.speedbridge.api.island.mode;

import me.tofpu.speedbridge.api.util.Identifier;

import java.util.List;

/**
 * This allows island slots to be categorized under a single identity. Standard, Hardcore, etc.
 * @see Identifier
 */
public interface Mode extends Identifier {
    /**
     * Slots defined by this mode
     *
     * @return the mode initial defined slots
     */
    List<Integer> slots();

    /**
     * @return true if the settings was set to this, otherwise false
     */
    boolean isDefault();
}
