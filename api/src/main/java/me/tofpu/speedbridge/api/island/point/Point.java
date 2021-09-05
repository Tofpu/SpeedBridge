package me.tofpu.speedbridge.api.island.point;

import org.bukkit.Location;

public interface Point {
    /**
     * @return the point identifier
     */
    String identifier();

    /**
     * @return the point location
     */
    Location pointA();

    /**
     * @param location new location
     */
    void pointA(final Location location);

    /**
     * @return true if the location is not null, otherwise false
     */
    boolean hasPointA();
}
