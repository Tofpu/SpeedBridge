package me.tofpu.speedbridge.island;

import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;

import java.util.List;

public interface IIsland {
    boolean isAvailable();

    IUser getTakenBy();

    void setTakenBy(final IUser takenBy);

    Location getLocation();

    void setLocation(final Location location);

    boolean hasLocation();

    int getSlot();

    Mode getMode();

    List<Location> getPlacedBlocks();

    IslandProperties getProperties();
}
