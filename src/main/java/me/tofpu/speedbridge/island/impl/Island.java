package me.tofpu.speedbridge.island.impl;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

// TODO:
// create a properties class, extend it and basically store locations and stuff there
public class Island extends IslandProperties implements IIsland {
    private final int slot;

    private Location location;
    private IUser takenBy;

    public Island(int slot) {
        this.slot = slot;
    }

    @Override
    public boolean isAvailable() {
        return takenBy == null;
    }

    @Override
    public IUser getTakenBy() {
        return takenBy;
    }

    @Override
    public void setTakenBy(@NotNull final IUser takenBy) {
        this.takenBy = takenBy;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull final Location location) {
        this.location = location;
    }

    @Override
    public boolean hasLocation() {
        return location != null;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public IslandProperties getProperties() {
        return this;
    }
}
