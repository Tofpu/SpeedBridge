package me.tofpu.speedbridge.game.controller;

import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.service.IIslandService;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameController {
    private final Map<UUID, IIsland> islandMap = new HashMap<>();
    private final IIslandService islandService;

    public GameController(IIslandService islandService) {
        this.islandService = islandService;
    }

    public void createIsland(final Player player, int slot) {
        final IIsland island = new Island(slot);
        islandMap.put(player.getUniqueId(), island);
    }

    public Result setupIsland(final Player player, SetupStage stage) {
        // /speedbridge create (slot) | /speedbridge create 10 (DONE)
        // /speedbridge set (slot) spawn/point-a/point-b |
        // /speedbridge finish
        final IIsland island = islandMap.get(player.getUniqueId());
        if (island == null) return Result.DENY;

        final String[] args = stage.name().split("_");
        final Location location = player.getLocation();
        switch (stage) {
            case SPAWN:
                island.setLocation(location);
                break;
            case POINT_A:
                island.getProperties().get(args[0]).setSectionA(location);
                break;
            case POINT_B:
                island.getProperties().get(args[0]).setSectionB(location);
                break;
            case SELECTION_A:
                island.getProperties().get(args[0]).setSectionA(location);
                break;
            case SELECTION_B:
                island.getProperties().get(args[0]).setSectionB(location);
                break;
        }
        return Result.SUCCESS;
    }

    public Result finishSetup(final Player player) {
        final IIsland island = islandMap.get(player.getUniqueId());
        if (island == null) return Result.DENY;

        final IslandProperties properties = island.getProperties();
        final TwoSection sectionPoint = properties.get("point");
        final TwoSection sectionSelection = properties.get("selection");
        if (island.hasLocation() && sectionPoint.hasSectionA() && sectionPoint.hasSectionB() && sectionSelection.hasSectionA() && sectionSelection.hasSectionB()) {
            islandService.addIsland(island);
            islandMap.remove(player.getUniqueId());
            return Result.SUCCESS;
        } else return Result.DENY;
    }
}
