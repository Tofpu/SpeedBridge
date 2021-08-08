package me.tofpu.speedbridge.island.service.impl;

import com.google.gson.Gson;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;
import org.bukkit.Location;
import org.bukkit.Material;

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
    public void resetBlocks(final IIsland island) {
        for (final Location location : island.getPlacedBlocks()) {
            island.getLocation().getWorld().getBlockAt(location).setType(Material.AIR);
        }
    }

    @Override
    public void resetIsland(final int slot) {
        final IIsland island = getIslandBySlot(slot);
        if (island == null) return;

        resetBlocks(island);
        island.setTakenBy(null);
    }

    @Override
    public void saveAll(final Gson gson, final File directory) {
        for (final IIsland island : this.islands) {
            resetBlocks(island);

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
                    writer.write(gson.toJson(island, IIsland.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadAll(final Gson gson, File directory) {
        for (final File file : directory.listFiles()) {
            try {
                if (!file.getName().endsWith(".json")) continue;
                final IIsland island = gson.fromJson(new FileReader(file), IIsland.class);
                if (island == null) continue;
                addIsland(island);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
