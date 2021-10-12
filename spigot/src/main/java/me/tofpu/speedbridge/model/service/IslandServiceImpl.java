package me.tofpu.speedbridge.model.service;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.api.model.service.IslandService;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.*;

public class IslandServiceImpl implements IslandService {
    private final IslandRepository repository;

    @Inject
    public IslandServiceImpl(final IslandRepository repository){
        this.repository = repository;
    }

    public void loadAll() {
        repository.loadAll();
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

    @Override
    public Optional<Island> findIslandBy(final IslandRepository.SearchAlgorithm algorithm) {
        return repository.findIslandBy(algorithm);
    }

    public Optional<Island> revert() {
        return repository.revert();
    }

    public void saveAll() {
        repository.saveAll();
    }
}