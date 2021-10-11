package me.tofpu.speedbridge.model.service;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.repository.LobbyRepository;
import me.tofpu.speedbridge.api.model.service.LobbyService;

public class LobbyServiceImpl implements LobbyService {
    private final LobbyRepository repository;

    @Inject
    public LobbyServiceImpl(final LobbyRepository repository) {
        this.repository = repository;
    }

    @Override
    public void load() {
        repository.loadAll();
    }

    public Lobby lobby() {
        return repository.lobby();
    }

    @Override
    public void save() {
        repository.saveAll();
    }
}
