package me.tofpu.speedbridge.island.properties.point;

import org.bukkit.Location;

public interface Point {
    String getIdentifier();

    Location getPointA();

    void setPointA(final Location location);

    boolean hasPointA();
}
