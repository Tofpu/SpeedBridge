package me.tofpu.speedbridge.model.object.island.properties.twosection;

import me.tofpu.speedbridge.api.model.object.island.point.TwoSection;
import org.bukkit.Location;

public class IslandSelection implements TwoSection {
    private final String identifier;

    private Location sectionA;
    private Location sectionB;

    public IslandSelection() {
        this.identifier = "position";
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
