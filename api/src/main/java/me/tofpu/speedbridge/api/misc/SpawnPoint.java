package me.tofpu.speedbridge.api.misc;

import me.tofpu.speedbridge.api.model.object.user.User;
import org.bukkit.Location;

import java.util.Optional;

public interface SpawnPoint {
    Optional<Location> location();
    Location location(final Location newLocation);
    boolean teleportToLobby(final User user);
}
