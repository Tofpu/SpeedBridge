package me.tofpu.speedbridge.model.repository;

import me.tofpu.speedbridge.api.misc.SpawnPoint;
import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.repository.LobbyRepository;
import me.tofpu.speedbridge.api.model.storage.Storage;
import me.tofpu.speedbridge.model.object.lobby.LobbyImpl;

import java.util.Optional;

public class LobbyRepositoryImpl implements LobbyRepository {
    private final Storage storage;
    private final Lobby lobby;

    public LobbyRepositoryImpl(final Storage storage) {
        this.storage = storage;
        this.lobby = new LobbyImpl();
    }

    @Override
    public void load() {
        final Optional<Lobby> lobby = storage.loadLobby();

        lobby.flatMap(SpawnPoint::location).ifPresent(this.lobby::location);
    }

    @Override
    public Lobby lobby() {
        return this.lobby;
    }

    @Override
    public void save() {
        storage.saveLobby();
    }
}
