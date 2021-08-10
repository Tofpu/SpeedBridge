package me.tofpu.speedbridge.island.properties.twosection;

import org.bukkit.Location;

public interface TwoSection {
    String getIdentifier();

    Location getSectionA();

    void setSectionA(final Location sectionA);

    boolean hasSectionA();

    Location getSectionB();

    void setSectionB(final Location sectionB);

    boolean hasSectionB();
}
