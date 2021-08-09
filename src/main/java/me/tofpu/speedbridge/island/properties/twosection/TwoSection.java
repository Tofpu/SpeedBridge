package me.tofpu.speedbridge.island.properties.twosection;

import org.bukkit.Location;

public interface TwoSection {
    public String getIdentifier();

    public Location getSectionA();

    public void setSectionA(final Location sectionA);

    public boolean hasSectionA();

    public Location getSectionB();

    public void setSectionB(final Location sectionB);

    public boolean hasSectionB();
}
