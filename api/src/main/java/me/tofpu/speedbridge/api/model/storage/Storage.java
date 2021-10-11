package me.tofpu.speedbridge.api.model.storage;

import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Storage {
    Optional<Lobby> loadLobby();
    Collection<Island> loadIslands();
    Optional<User> loadUser(final UUID uniqueId);
    void saveLobby();
    void deleteIsland(final int slot);
    void saveIslands();
    void saveUser(final User user);
    void saveUsers();
}
