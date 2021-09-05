package me.tofpu.speedbridge.island.properties.twosection;

import me.tofpu.speedbridge.island.properties.point.Point;
import org.bukkit.Location;

public interface TwoSection extends Point {
    String identifier();

    @Override
    Location pointA();

    @Override
    void pointA(final Location pointA);

    @Override
    boolean hasPointA();

    Location pointB();

    void pointB(final Location pointB);

    boolean hasPointB();
}
