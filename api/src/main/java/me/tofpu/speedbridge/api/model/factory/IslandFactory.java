package me.tofpu.speedbridge.api.model.factory;

import me.tofpu.speedbridge.api.model.object.island.Island;

public interface IslandFactory {
    Island createIsland(final int slot);
}
