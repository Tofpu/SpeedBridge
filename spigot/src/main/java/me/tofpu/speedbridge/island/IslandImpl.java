package me.tofpu.speedbridge.island;

import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandProperties;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.island.properties.IslandPropertiesImpl;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class IslandImpl extends IslandPropertiesImpl implements Island {
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
    public User takenBy() {
        return takenBy;
    }

    @Override
    public void takenBy(final User takenBy) {
        this.takenBy = takenBy;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public void location(final Location location) {
        this.location = location;
    }

    @Override
    public boolean hasLocation() {
        return location != null;
    }

    @Override
    public int slot() {
        return slot;
    }

    @Override
    public Mode mode() {
        return mode;
    }

    @Override
    public List<Location> placedBlocks() {
        return placedBlocks;
    }

    @Override
    public IslandProperties properties() {
        return this;
    }

    @Override
    public String identifier() {
        return slot() + "";
    }
}
