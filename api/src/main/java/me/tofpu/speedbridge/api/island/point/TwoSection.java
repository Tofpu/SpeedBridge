package me.tofpu.speedbridge.api.island.point;

import org.bukkit.Location;

public interface TwoSection extends Point {
    String identifier();

    Location pointB();
    boolean hasPointB();
    void pointB(final Location pointB);
}
