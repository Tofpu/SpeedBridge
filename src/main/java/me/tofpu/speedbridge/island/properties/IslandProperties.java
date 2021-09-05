package me.tofpu.speedbridge.island.properties;

import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.properties.point.PointImpl;
import me.tofpu.speedbridge.island.properties.twosection.IslandSelection;

import java.util.ArrayList;
import java.util.List;

public class IslandProperties {
    private final List<Point> twoSections;

    public IslandProperties() {
        this.twoSections = new ArrayList<>();
        this.twoSections.add(new PointImpl());
        this.twoSections.add(new IslandSelection());
    }

    public Point get(final String identifier) {
        for (final Point twoSection : twoSections) {
            if (twoSection.identifier().equalsIgnoreCase(identifier)) return twoSection;
        }
        return null;
    }

    public List<Point> twoSections() {
        return twoSections;
    }
}
