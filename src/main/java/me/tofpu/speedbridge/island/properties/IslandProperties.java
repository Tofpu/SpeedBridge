package me.tofpu.speedbridge.island.properties;

import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.properties.point.impl.EndPoint;
import me.tofpu.speedbridge.island.properties.twosection.impl.IslandSelection;

import java.util.ArrayList;
import java.util.List;

public class IslandProperties {
    private final List<Point> twoSections;

    public IslandProperties() {
        this.twoSections = new ArrayList<>();
        this.twoSections.add(new EndPoint());
        this.twoSections.add(new IslandSelection());
    }

    public Point get(final String identifier) {
        for (final Point twoSection : twoSections) {
            if (twoSection.getIdentifier().equalsIgnoreCase(identifier)) return twoSection;
        }
        return null;
    }

    public List<Point> getTwoSections() {
        return twoSections;
    }
}
