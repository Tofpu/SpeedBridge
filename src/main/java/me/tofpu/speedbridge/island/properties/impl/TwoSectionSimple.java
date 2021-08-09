package me.tofpu.speedbridge.island.properties.impl;

import me.tofpu.speedbridge.island.properties.property.TwoSection;
import org.bukkit.Location;

public class TwoSectionSimple implements TwoSection {
    private String identifier = "";

    private Location sectionA;
    private Location sectionB;

    public TwoSectionSimple() {
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    @Override
    public String toString() {
        return "TwoSectionSimple{" +
                "identifier='" + identifier + '\'' +
                ", sectionA=" + sectionA +
                ", sectionB=" + sectionB +
                '}';
    }
}
