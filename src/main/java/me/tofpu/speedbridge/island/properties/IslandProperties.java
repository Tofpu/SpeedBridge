package me.tofpu.speedbridge.island.properties;

import org.bukkit.Location;

public class IslandProperties {
    private Location locationA;
    private Location locationB;

    public IslandProperties() {
    }

    public IslandProperties(final Location locationA, final Location locationB) {
        this.locationA = locationA;
        this.locationB = locationB;
    }

    public boolean hasLocationA() {
        return locationA != null;
    }

    public boolean hasLocationB() {
        return locationB != null;
    }


    public Location getLocationA() {
        return locationA;
    }

    public void setLocationA(final Location locationA) {
        this.locationA = locationA;
    }


    public Location getLocationB() {
        return locationB;
    }

    public void setLocationB(final Location locationB) {
        this.locationB = locationB;
    }
}
