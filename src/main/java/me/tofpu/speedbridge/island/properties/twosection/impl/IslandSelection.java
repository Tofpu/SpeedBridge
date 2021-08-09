package me.tofpu.speedbridge.island.properties.twosection.impl;

import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import org.bukkit.Location;

public class IslandSelection implements TwoSection {
    private final String identifier;

    private Location sectionA;
    private Location sectionB;

    public IslandSelection() {
        this.identifier = "selection";
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Location getSectionA() {
        return sectionA;
    }

    @Override
    public void setSectionA(final Location sectionA) {
        this.sectionA = sectionA;
    }

    @Override
    public boolean hasSectionA() {
        return sectionA != null;
    }

    @Override
    public Location getSectionB() {
        return sectionB;
    }

    @Override
    public void setSectionB(final Location sectionB) {
        this.sectionB = sectionB;
    }

    @Override
    public boolean hasSectionB() {
        return sectionB != null;
    }
}
