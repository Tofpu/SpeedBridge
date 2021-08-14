package me.tofpu.speedbridge.island.impl;

import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Island extends IslandProperties implements IIsland {
    private final List<Location> placedBlocks = new ArrayList<>();

    private final int slot;
    private final Mode mode;

    private Location location;
    private IUser takenBy;

    public Island(int slot) {
        this.slot = slot;
        this.mode = ModeManager.getModeManager().get(slot);
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
    public void setTakenBy(final IUser takenBy) {
        this.takenBy = takenBy;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(final Location location) {
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
    public Mode getMode() {
        return mode;
    }

    @Override
    public List<Location> getPlacedBlocks() {
        return placedBlocks;
    }

    @Override
    public IslandProperties getProperties() {
        return this;
    }

    @Override
    public String getIdentifier() {
        return getSlot() + "";
    }
}
