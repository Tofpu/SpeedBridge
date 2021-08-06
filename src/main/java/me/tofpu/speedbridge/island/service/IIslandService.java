package me.tofpu.speedbridge.island.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.user.IUser;

import java.io.File;
import java.util.List;

public interface IIslandService {
    public void addIsland(final IIsland island);

    public void removeIsland(final IIsland island);

    public IIsland getIslandBySlot(final int slot);

    public IIsland getIslandByUser(final IUser takenBy);

    public List<IIsland> getAvailableIslands();

    public void saveAll(final Gson gson, final File directory);

    public void loadAll(final Gson gson, final File directory);
}
