package me.tofpu.speedbridge.model.object.lobby;

import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.object.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class LobbyImpl implements Lobby {
    private Location location;

    @Override
    public Optional<Location> location() {
        return Optional.ofNullable(location);
    }

    @Override
    public Location location(final Location newLocation) {
        return this.location = newLocation;
    }

    @Override
    public boolean teleportToLobby(final User user) {
        final Optional<Player> player = Optional.ofNullable(user.player());
        if (!location().isPresent() || !player.isPresent()) {
            return false;
        }

        player.get().teleport(location().get());
        return true;
    }
}
