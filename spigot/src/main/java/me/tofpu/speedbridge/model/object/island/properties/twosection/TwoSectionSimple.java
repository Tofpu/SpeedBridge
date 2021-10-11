package me.tofpu.speedbridge.model.object.island.properties.twosection;

import me.tofpu.speedbridge.api.island.point.TwoSection;
import org.bukkit.Location;

public class TwoSectionSimple implements TwoSection {
    private String identifier = "";

    private Location sectionA;
    private Location sectionB;

    @Override
    public String identifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    @Override
    public String toString() {
        return "TwoSectionSimple{" +
                "identifier='" + identifier + '\'' +
                ", sectionA=" + sectionA +
                ", sectionB=" + sectionB +
                '}';
    }
}
