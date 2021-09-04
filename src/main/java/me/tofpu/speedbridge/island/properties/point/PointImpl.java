package me.tofpu.speedbridge.island.properties.point;

import org.bukkit.Location;

public class PointImpl implements Point {
    private final String identifier;

    private Location location;

    public PointImpl() {
        this.identifier = "point";
    }

    public PointImpl(final Location location) {
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
