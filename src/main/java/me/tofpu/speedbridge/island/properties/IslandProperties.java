package me.tofpu.speedbridge.island.properties;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.bukkit.Location;

public class IslandProperties {
    private Location locationA;
    private Location locationB;

    public IslandProperties() {
    }

    public IslandProperties(@NotNull final Location locationA, @NotNull final Location locationB) {
        this.locationA = locationA;
        this.locationB = locationB;
    }

    public boolean hasLocationA() {
        return locationA != null;
    }

    public boolean hasLocationB() {
        return locationB != null;
    }

    @Nullable
    public Location getLocationA() {
        return locationA;
    }

    public void setLocationA(@NotNull final Location locationA) {
        this.locationA = locationA;
    }

    @Nullable
    public Location getLocationB() {
        return locationB;
    }

    public void setLocationB(@NotNull final Location locationB) {
        this.locationB = locationB;
    }
}
