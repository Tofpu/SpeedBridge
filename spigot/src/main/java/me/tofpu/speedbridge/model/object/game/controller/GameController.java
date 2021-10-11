package me.tofpu.speedbridge.model.object.game.controller;

import me.tofpu.speedbridge.api.game.Result;
import me.tofpu.speedbridge.api.game.SetupStage;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandProperties;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.island.point.Point;
import me.tofpu.speedbridge.api.island.point.TwoSection;
import me.tofpu.speedbridge.model.object.island.IslandImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameController {
    private final Map<UUID, Island> islandMap = new HashMap<>();
    private final IslandService islandService;

    public GameController(final IslandService islandService) {
        this.islandService = islandService;
    }

    public Result createIsland(final Player player, int slot) {
        final Island island = new IslandImpl(slot);

        // if an island exists by that defined slot
        if (islandService.getIslandBySlot(slot) != null || islandMap.containsValue(island)) return Result.FAIL;

        // store the island & player to a cache map for modifying/setting-up purposes
        islandMap.put(player.getUniqueId(), island);
        return Result.SUCCESS;
    }

    public Result setupIsland(final Player player, SetupStage stage) {
        final Island island = islandMap.get(player.getUniqueId());

        // if the player is not in the cache list
        if (island == null) return Result.FAIL;

        final String[] args = stage.name().split("_");
        final Location location = player.getLocation();
        switch (stage) {
            case SPAWN:
                island.location(location);
                break;
            case ENDPOINT:
                island.properties().get(args[0]).pointA(location);
                break;
            case POSITION_1:
            case POSITION_2:
                final TwoSection section = (TwoSection) island.properties().get(args[0]);
                if (args[1].equalsIgnoreCase("1")) section.pointA(location);
                else section.pointB(location);
                break;
        }
        return Result.SUCCESS;
    }

    public Result modifyIsland(final Player player, final int slot) {
        // if the player is in the cache list, meaning they're occupied with an island
        if (islandMap.get(player.getUniqueId()) != null) return Result.FULL;
        final Island island = islandService.getIslandBySlot(slot);

        // if the island defined doesn't exist
        if (island == null) return Result.FAIL;

        islandMap.put(player.getUniqueId(), island);
        player.teleport(island.location());
        return Result.SUCCESS;
    }

    public Result cancelSetup(final Player player) {
        // if the player is in the cache list
        if (islandMap.containsKey(player.getUniqueId())) {
            islandMap.remove(player.getUniqueId());
            return Result.SUCCESS;
        }
        return Result.FAIL;
    }

    public Result finishSetup(final Player player) {
        final Island island = islandMap.get(player.getUniqueId());

        // if the player is not in the cache list
        if (island == null) return Result.FAIL;
        final IslandProperties properties = island.properties();
        final Point sectionPoint = properties.get("endpoint");
        final TwoSection sectionSelection = (TwoSection) properties.get("position");

        // If the island has a spawn, point, selection a and b set
        if (island.hasLocation() && sectionPoint.hasPointA() && sectionSelection.hasPointA() && sectionSelection.hasPointB()) {
            islandService.removeIsland(islandService.getIslandBySlot(island.slot()));
            islandService.addIsland(island);
            islandMap.remove(player.getUniqueId());
            return Result.SUCCESS;
        } else return Result.FAIL;
    }
}
