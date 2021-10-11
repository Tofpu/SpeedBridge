package me.tofpu.speedbridge.model.service;

import me.tofpu.speedbridge.api.game.Result;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.model.object.game.Game;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class IslandServiceImpl implements IslandService {
    private final List<Island> islands;
    private final Deque<Island> islandDeque = new ArrayDeque<>();

    private File directory;

    public IslandServiceImpl(){
        islands = new ArrayList<>();
    }

    public void initialize(final DataManager dataManager){
        this.directory = dataManager.getFiles()[1];

        Game.EXECUTOR.scheduleWithFixedDelay(
                () -> saveAll(false)
                ,5, 5, TimeUnit.MINUTES);
    }

    public Result removeIsland(final int slot) {
        final Island island = getIslandBySlot(slot);
        if (island == null) {
            return Result.INVALID_ISLAND;
        }

        final File islandFile = new File(directory, "island-" + island.slot() + ".json");
        try {
            Files.deleteIfExists(islandFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.FAIL;
        }
        removeIsland(island);

        return Result.SUCCESS;
    }

    @Override
    public void addIsland(final Island island) {
        this.islands.add(island);
    }

    @Override
    public void removeIsland(final Island island) {
        if (island == null) return;

        this.islands.remove(island);
        this.islandDeque.push(island);
    }

    public Island revert() {
        if (islandDeque.isEmpty()) {
            return null;
        }
        return islandDeque.pop();
    }

    @Override
    public Island getIslandBySlot(final int slot) {
        for (final Island island : this.islands) {
            if (island.slot() == slot) return island;
        }
        return null;
    }

    @Override
    public Island getIslandByUser(final User takenBy) {
        for (final Island island : this.islands) {
            if (!island.isAvailable() && island.takenBy() == takenBy) return island;
        }
        return null;
    }

    @Override
    public List<Island> getAvailableIslands() {
        final List<Island> islands = new ArrayList<>();
        for (final Island island : this.islands) {
            if (island.isAvailable() && island.hasLocation()) {
                islands.add(island);
            }
        }
        return islands;
    }

    @Override
    public List<Island> getAvailableIslands(final Mode mode) {
        final List<Island> islands = new ArrayList<>();
        for (final Island island : this.islands) {
            final Mode islandMode = island.mode();

            if (islandMode != null && islandMode.identifier().equals(mode.identifier())
                    && island.isAvailable() && island.hasLocation()) islands.add(island);
        }
        return islands;
    }

    public void saveAll(final boolean emptyList) {
        for (final Island island : this.islands) {
            if (island.location().getWorld() == null) continue;
            final File file = new File(directory, "island-" + island.slot() + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                try (final FileWriter writer = new FileWriter(file)) {
                    writer.write(DataManager.GSON.toJson(island, Island.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (emptyList) this.islands.clear();
    }

    public void loadAll() {
        for (final File file : directory.listFiles()) {
            try {
                if (!file.getName().endsWith(".json")) continue;

                final Island island;
                try (final FileReader reader = new FileReader(file)) {
                    island = DataManager.GSON.fromJson(reader, Island.class);;
                }
                if (island == null) continue;

                addIsland(island);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
