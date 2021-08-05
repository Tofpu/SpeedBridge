package me.tofpu.speedbridge.island.service.impl;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IslandService implements IIslandService {
    private final List<IIsland> islands = new ArrayList<>();

    @Override
    public void addIsland(final IIsland island) {
        this.islands.add(island);
    }

    @Override
    public void removeIsland(final IIsland island) {
        this.islands.remove(island);
    }

    @Override
    public IIsland getIslandBySlot(final int slot) {
        for (final IIsland island : this.islands) {
            if (island.getSlot() == slot) return island;
        }
        return null;
    }

    @Override
    public IIsland getIslandByUser(final IUser takenBy) {
        for (final IIsland island : this.islands) {
            if (!island.isAvailable() && island.getTakenBy() == takenBy) return island;
        }
        return null;
    }

    @Override
    public List<IIsland> getAvailableIslands() {
        final List<IIsland> islands = new ArrayList<>();
        for (final IIsland island : this.islands) {
            if (island.isAvailable() && island.hasLocation()) {
                islands.add(island);
            }
        }
        return islands;
    }

    @Override
    public void saveAll(final TypeAdapter<IIsland> adapter, final File directory) {
        for (final IIsland island : this.islands) {
            final File file = new File(directory, "island-" + island.getSlot() + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                final JsonWriter writer = new JsonWriter(new FileWriter(file));
                adapter.write(writer, island);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadAll(TypeAdapter<IIsland> adapter, File directory) {
        for (final File file : directory.listFiles()) {
            try {
                addIsland(adapter.fromJson(new FileReader(file)));
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
