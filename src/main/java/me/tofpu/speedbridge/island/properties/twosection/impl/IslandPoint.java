package me.tofpu.speedbridge.island.properties.twosection.impl;

import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import org.bukkit.Location;

public class IslandPoint implements TwoSection {
    private final String identifier;

    private Location sectionA;
    private Location sectionB;

    public IslandPoint() {
        this.identifier = "point";
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Location getPointA() {
        return sectionA;
    }

    @Override
    public void setPointA(final Location sectionA) {
        this.sectionA = sectionA;
    }

    @Override
    public boolean hasPointA() {
        return sectionA != null;
    }

    @Override
    public Location getPointB() {
        return sectionB;
    }

    @Override
    public void setPointB(final Location sectionB) {
        this.sectionB = sectionB;
    }

    @Override
    public boolean hasPointB() {
        return sectionB != null;
    }
}