package me.tofpu.speedbridge.island;

import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.util.Identifier;
import org.bukkit.Location;

import java.util.List;

public interface Island extends Identifier {
    boolean isAvailable();

    User getTakenBy();

    void setTakenBy(final User takenBy);

    Location getLocation();

    void setLocation(final Location location);

    boolean hasLocation();

    int getSlot();

    Mode getMode();

    List<Location> getPlacedBlocks();

    IslandProperties getProperties();
}
