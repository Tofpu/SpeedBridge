package me.tofpu.speedbridge.api.island;

import me.tofpu.speedbridge.api.island.point.Point;

import java.util.List;

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
