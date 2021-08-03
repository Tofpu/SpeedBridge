package me.tofpu.speedbridge.island.service.impl;

import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;

import java.util.ArrayList;
import java.util.List;

public class IslandService implements IIslandService {
    private final List<Island> islands = new ArrayList<>();

    @Override
    public void addIsland(final Island island){
        this.islands.add(island);
    }

    @Override
    public void removeIsland(final Island island){
        this.islands.remove(island);
    }

    @Override
    public Island getIslandBySlot(final int slot){
        for (final Island island : this.islands){
            if (island.getSlot() == slot) return island;
        }
        return null;
    }

    @Override
    public Island getIslandByUser(final IUser takenBy){
        for (final Island island : this.islands){
            if (!island.isAvailable() && island.getTakenBy() == takenBy) return island;
        }
        return null;
    }

    @Override
    public List<Island> getAvailableIslands(){
        final List<Island> islands = new ArrayList<>();
        for (final Island island : this.islands){
            if (island.isAvailable()) islands.add(island);
        }
        return islands;
    }
}
