package me.tofpu.speedbridge.island.controller;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;

import java.util.List;

public class IslandController {
    private final IIslandService islandService;

    public IslandController(final IIslandService islandService) {
        this.islandService = islandService;
    }

    public void addIsland(@NotNull final Island island){
        this.islandService.addIsland(island);
    }

    public void removeIsland(@NotNull final Island island){
        this.islandService.removeIsland(island);
    }

    @Nullable
    public Island getIslandBySlot(@NotNull final int slot){
        return this.islandService.getIslandBySlot(slot);
    }

    @Nullable
    public Island getIslandByUser(@NotNull final IUser user){
        return this.islandService.getIslandByUser(user);
    }

    @NotNull
    public List<Island> getAvailableIslands(){
        return this.islandService.getAvailableIslands();
    }
}
