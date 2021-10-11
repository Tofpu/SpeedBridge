package me.tofpu.speedbridge.api.model.object.island;

import me.tofpu.speedbridge.api.model.object.island.point.Point;

import java.util.List;

/**
 * The island property. Useful for getting their points.
 * @see Point
 */
public interface IslandProperties {
    /**
     * @param identifier point identifier
     *
     * @return the island's point, otherwise null
     */
    Point get(final String identifier);

    /**
     * The island's defined points
     *
     * @return the island points
     */
    List<Point> points();
}
