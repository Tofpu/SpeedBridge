package me.tofpu.speedbridge.island.service;

import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.Game;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IslandServiceImpl implements IslandService {
    private final List<Island> islands;
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

    @Override
    public void addIsland(final Island island) {
        this.islands.add(island);
    }

    @Override
    public void removeIsland(final Island island) {
        this.islands.remove(island);
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
            if (island.mode().identifier().equals(mode.identifier())
                    && island.isAvailable() && island.hasLocation()) islands.add(island);
        }
        return islands;
    }

    @Override
    public void saveAll(final boolean emptyList) {
        for (final Island island : this.islands) {
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

    @Override
    public void loadAll() {
        for (final File file : directory.listFiles()) {
            try {
                if (!file.getName().endsWith(".json")) continue;
                final Island island = DataManager.GSON.fromJson(new FileReader(file), Island.class);
                if (island == null) continue;
                addIsland(island);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
