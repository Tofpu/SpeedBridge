package me.tofpu.speedbridge.island.properties;

import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.properties.twosection.impl.IslandPoint;
import me.tofpu.speedbridge.island.properties.twosection.impl.IslandSelection;

import java.util.ArrayList;
import java.util.List;

public class IslandProperties {
    private final List<TwoSection> twoSections;

    public IslandProperties() {
        this.twoSections = new ArrayList<>();
        this.twoSections.add(new IslandPoint());
        this.twoSections.add(new IslandSelection());
    }

    public TwoSection get(final String identifier) {
        for (final TwoSection twoSection : twoSections) {
            if (twoSection.getIdentifier().equalsIgnoreCase(identifier)) return twoSection;
        }
        return null;
    }

    public List<TwoSection> getTwoSections() {
        return twoSections;
    }
}
