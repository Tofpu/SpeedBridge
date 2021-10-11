package me.tofpu.speedbridge.model.service;

import com.google.gson.Gson;
import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.lobby.Lobby;
import me.tofpu.speedbridge.api.model.repository.LobbyRepository;
import me.tofpu.speedbridge.api.model.service.LobbyService;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LobbyServiceImpl implements LobbyService {
    private final LobbyRepository repository;

    @Inject
    public LobbyServiceImpl(final LobbyRepository repository) {
        this.repository = repository;
    }

    @Override
    public void load() {
        repository.load();
    }

    public Lobby lobby() {
        return repository.lobby();
    }

    @Override
    public void save() {
        repository.save();
    }
}
