package me.tofpu.speedbridge.model.object.island.properties.point;

import me.tofpu.speedbridge.api.model.object.island.point.Point;
import org.bukkit.Location;

public class PointImpl implements Point {
    private final String identifier;

    private Location location;

    public PointImpl() {
        this.identifier = "endpoint";
    }

    public PointImpl(final Location location) {
        this();
        this.location = location;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public Location pointA() {
        return location;
    }

    @Override
    public void pointA(final Location location) {
        this.location = location;
    }

    @Override
    public boolean hasPointA() {
        return location != null;
    }
}
