package me.tofpu.speedbridge.island.service.impl;

import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;

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
}
