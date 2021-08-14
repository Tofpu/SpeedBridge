package me.tofpu.speedbridge.island.properties.twosection;

import me.tofpu.speedbridge.island.properties.point.Point;
import org.bukkit.Location;

public interface TwoSection extends Point {
    String getIdentifier();

    @Override
    Location getPointA();

    @Override
    void setPointA(final Location pointA);

    @Override
    boolean hasPointA();

    Location getPointB();

    void setPointB(final Location pointB);

    boolean hasPointB();
}
