package me.tofpu.speedbridge.island.properties;

import me.tofpu.speedbridge.island.properties.impl.IslandPoint;
import me.tofpu.speedbridge.island.properties.impl.IslandSelection;
import me.tofpu.speedbridge.island.properties.property.TwoSection;

import java.util.ArrayList;
import java.util.List;

public class IslandProperties {
    private final List<TwoSection> twoSections;

    public IslandProperties() {
        this.twoSections = new ArrayList<>();
    }

    public TwoSection get(final String identifier){
        for (final TwoSection twoSection : twoSections){
            if (twoSection.getIdentifier().equalsIgnoreCase(identifier)) return twoSection;
        }
        return null;
    }

    public List<TwoSection> getTwoSections() {
        return twoSections;
    }
}
