package me.tofpu.speedbridge.island;

import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.util.Identifier;
import org.bukkit.Location;

import java.util.List;

public interface Island extends Identifier {
    boolean isAvailable();

    User takenBy();
    void takenBy(final User takenBy);

    Location location();
    void location(final Location location);
    boolean hasLocation();

    int slot();
    Mode mode();
    List<Location> placedBlocks();
    IslandProperties properties();
}
