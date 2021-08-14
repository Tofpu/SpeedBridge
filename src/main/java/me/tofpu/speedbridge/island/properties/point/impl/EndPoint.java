package me.tofpu.speedbridge.island.properties.point.impl;

import me.tofpu.speedbridge.island.properties.point.Point;
import org.bukkit.Location;

public class EndPoint implements Point {
    private final String identifier;

    private Location location;

    public EndPoint() {
        this.identifier = "point";
    }

    ;

    public EndPoint(final Location location) {
        this();
        this.location = location;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Location getPointA() {
        return location;
    }

    @Override
    public void setPointA(final Location location) {
        this.location = location;
    }

    @Override
    public boolean hasPointA() {
        return location != null;
    }
}
