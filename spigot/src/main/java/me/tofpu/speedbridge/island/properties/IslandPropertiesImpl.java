package me.tofpu.speedbridge.island.properties;

import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.properties.point.PointImpl;
import me.tofpu.speedbridge.island.properties.twosection.IslandSelection;

import java.util.ArrayList;
import java.util.List;

public class IslandPropertiesImpl implements IslandProperties {
    private final List<Point> twoSections;

    public IslandPropertiesImpl() {
        this.twoSections = new ArrayList<>();
        this.twoSections.add(new PointImpl());
        this.twoSections.add(new IslandSelection());
    }

    @Override
    public Point get(final String identifier) {
        for (final Point twoSection : twoSections) {
            if (twoSection.identifier().equalsIgnoreCase(identifier)) return twoSection;
        }
        return null;
    }

    @Override
    public List<Point> points() {
        return twoSections;
    }
}
