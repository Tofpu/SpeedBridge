package me.tofpu.speedbridge.island.properties.point;

import org.bukkit.Location;

public interface Point {
    String identifier();

    Location pointA();

    void pointA(final Location location);

    boolean hasPointA();
}
