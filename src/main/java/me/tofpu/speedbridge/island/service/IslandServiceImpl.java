package me.tofpu.speedbridge.island.service;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.user.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IslandServiceImpl implements IslandService {
    private final List<Island> islands;

    public IslandServiceImpl(){
        islands = new ArrayList<>();
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
            if (island.getSlot() == slot) return island;
        }
        return null;
    }

    @Override
    public Island getIslandByUser(final User takenBy) {
        for (final Island island : this.islands) {
            if (!island.isAvailable() && island.getTakenBy() == takenBy) return island;
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
            if (island.getMode().getIdentifier().equals(mode.getIdentifier())
                    && island.isAvailable() && island.hasLocation()) islands.add(island);
        }
        return islands;
    }

    @Override
    public void saveAll(final File directory) {
        for (final Island island : this.islands) {
            final File file = new File(directory, "island-" + island.getSlot() + ".json");
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
        this.islands.clear();
    }

    @Override
    public void loadAll(File directory) {
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
