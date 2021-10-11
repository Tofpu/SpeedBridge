package me.tofpu.speedbridge.api.model.object.island.point;

import me.tofpu.speedbridge.api.util.Identifier;
import org.bukkit.Location;

/**
 * This represents a single location point. Used for the island location end-point.
 * @see Identifier
 */
public interface Point extends Identifier {
    /**
     * The pointA defined location
     *
     * @return the point location
     */
    Location pointA();

    /**
     * The availability of pointA location
     *
     * @return true if pointA is defined, otherwise false
     */
    boolean hasPointA();

    /**
     * @param location defining the pointA location
     */
    void pointA(final Location location);
}
