package me.tofpu.speedbridge.model.service;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.api.model.service.IslandService;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.data.DataManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class IslandServiceImpl implements IslandService {
    private final IslandRepository repository;

    @Inject
    public IslandServiceImpl(final IslandRepository repository){
        this.repository = repository;
    }

    @Override
    public void addIsland(final Island island) {
        repository.register(island);
    }

    @Override
    public Result removeIsland(final int slot) {
        final Optional<Island> island = repository.remove(slot);
        return island.isPresent() ? Result.SUCCESS : Result.INVALID_ISLAND;
    }

    public Optional<Island> revert() {
        return repository.revert();
    }

    @Override
    public Optional<Island> getIslandBySlot(final int slot) {
        return repository.findIslandBy(IslandRepository.SearchAlgorithm.of(slot));
    }

    @Override
    public Optional<Island> getIslandByUser(final User takenBy) {
        return repository.findIslandBy(IslandRepository.SearchAlgorithm.of(takenBy));
    }

    @Override
    public Optional<Island> getAvailableIslands() {
        return repository.findIslandBy(IslandRepository.SearchAlgorithm.of(-1));
    }

    @Override
    public Optional<Island> getAvailableIslands(final Mode mode) {
        return repository.findIslandBy(IslandRepository.SearchAlgorithm.of(mode));
    }

    public void saveAll() {
        repository.save();
    }

    public void loadAll() {
        repository.load();
    }
}