package me.tofpu.speedbridge.island.controller;

import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.user.IUser;

import java.util.List;

public class IslandController {
    private final IIslandService islandService;

    public IslandController(final IIslandService islandService) {
        this.islandService = islandService;
    }

    public void addIsland(final Island island) {
        this.islandService.addIsland(island);
    }

    public void removeIsland(final Island island) {
        this.islandService.removeIsland(island);
    }


    public IIsland getIslandBySlot(final int slot) {
        return this.islandService.getIslandBySlot(slot);
    }


    public IIsland getIslandByUser(final IUser user) {
        return this.islandService.getIslandByUser(user);
    }


    public List<IIsland> getAvailableIslands() {
        return this.islandService.getAvailableIslands();
    }
}
