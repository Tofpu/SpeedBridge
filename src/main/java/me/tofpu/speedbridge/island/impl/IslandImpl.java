package me.tofpu.speedbridge.island.impl;

import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.User;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class IslandImpl extends IslandProperties implements Island {
    private final List<Location> placedBlocks = new ArrayList<>();

    private final int slot;
    private final Mode mode;

    private Location location;
    private User takenBy;

    public IslandImpl(int slot) {
        this.slot = slot;
        this.mode = ModeManager.getModeManager().get(slot);
    }

    @Override
    public boolean isAvailable() {
        return takenBy == null;
    }

    @Override
    public User getTakenBy() {
        return takenBy;
    }

    @Override
    public void setTakenBy(final User takenBy) {
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
