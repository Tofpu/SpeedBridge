package me.tofpu.speedbridge.model.object.island.properties.twosection;

import me.tofpu.speedbridge.api.island.point.TwoSection;
import org.bukkit.Location;

public class IslandPoint implements TwoSection {
    private final String identifier;

    private Location sectionA;
    private Location sectionB;

    public IslandPoint() {
        this.identifier = "point";
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public Location pointA() {
        return sectionA;
    }

    @Override
    public void pointA(final Location sectionA) {
        this.sectionA = sectionA;
    }

    @Override
    public boolean hasPointA() {
        return sectionA != null;
    }

    @Override
    public Location pointB() {
        return sectionB;
    }

    @Override
    public void pointB(final Location sectionB) {
        this.sectionB = sectionB;
    }

    @Override
    public boolean hasPointB() {
        return sectionB != null;
    }
}