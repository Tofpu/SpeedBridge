package me.tofpu.speedbridge.game.controller;

import me.tofpu.speedbridge.game.Result;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.IslandImpl;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.island.properties.point.Point;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.service.IslandService;
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
        if (islandService.getIslandBySlot(slot) != null) return Result.DENY;
        final Island island = new IslandImpl(slot);
        islandMap.put(player.getUniqueId(), island);
        return Result.SUCCESS;
    }

    public Result setupIsland(final Player player, SetupStage stage) {
        final Island island = islandMap.get(player.getUniqueId());
        if (island == null) return Result.DENY;

        final String[] args = stage.name().split("_");
        final Location location = player.getLocation();
        switch (stage) {
            case SPAWN:
                island.location(location);
                break;
            case POINT:
                island.properties().get(args[0]).pointA(location);
                break;
            case SELECTION_A:
            case SELECTION_B:
                final TwoSection section = (TwoSection) island.properties().get(args[0]);
                if (args[1].equalsIgnoreCase("a")) section.pointA(location);
                else section.pointB(location);
                break;
        }
        return Result.SUCCESS;
    }

    public Result modifyIsland(final Player player, final int slot) {
        if (islandMap.get(player.getUniqueId()) != null) return Result.FULL;

        final Island island = islandService.getIslandBySlot(slot);
        if (island == null) return Result.DENY;

        islandMap.put(player.getUniqueId(), island);
        player.teleport(island.location());
        return Result.SUCCESS;
    }

    public Result cancelSetup(final Player player) {
        if (islandMap.containsKey(player.getUniqueId())) {
            islandMap.remove(player.getUniqueId());
            return Result.SUCCESS;
        }
        return Result.DENY;
    }

    public Result finishSetup(final Player player) {
        final Island island = islandMap.get(player.getUniqueId());
        if (island == null) return Result.INVALID_LOBBY;

        final IslandProperties properties = island.properties();
        final Point sectionPoint = properties.get("point");
        final TwoSection sectionSelection = (TwoSection) properties.get("selection");
        if (island.hasLocation() && sectionPoint.hasPointA() && sectionSelection.hasPointA() && sectionSelection.hasPointB()) {
            islandService.removeIsland(islandService.getIslandBySlot(island.slot()));
            islandService.addIsland(island);
            islandMap.remove(player.getUniqueId());
            return Result.SUCCESS;
        } else return Result.DENY;
    }
}
