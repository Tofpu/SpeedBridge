package me.tofpu.speedbridge.api.model.storage;

import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.Optional;
import java.util.UUID;

public interface Storage {
    Optional<Lobby> loadLobby();
    Optional<User> loadUser(final UUID uniqueId);
    void saveLobby();
    void saveUser(final User user);
    void saveUsers();
}
