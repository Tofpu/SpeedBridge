package me.tofpu.speedbridge.game.controller;

import me.tofpu.speedbridge.game.controller.stage.SetupStage;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.impl.Island;
import me.tofpu.speedbridge.island.properties.IslandProperties;
import me.tofpu.speedbridge.island.properties.point.Point;
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

    public Result createIsland(final Player player, int slot) {
        if (islandService.getIslandBySlot(slot) != null) return Result.DENY;
        final IIsland island = new Island(slot);
        islandMap.put(player.getUniqueId(), island);
        return Result.SUCCESS;
    }

    public Result setupIsland(final Player player, SetupStage stage) {
        final IIsland island = islandMap.get(player.getUniqueId());
        if (island == null) return Result.DENY;

        final String[] args = stage.name().split("_");
        final Location location = player.getLocation();
        switch (stage) {
            case SPAWN:
                island.setLocation(location);
                break;
            case POINT:
                island.getProperties().get(args[0]).setPointA(location);
                break;
//            case SELECTION_A:
//                island.getProperties().get(args[0]).setPointA(location);
//                break;
//            case SELECTION_B:
//                island.getProperties().get(args[0]).setPointB(location);
//                break;
            case SELECTION_A:
            case SELECTION_B:
                final TwoSection section = (TwoSection) island.getProperties().get(args[0]);
                if (args[1].equalsIgnoreCase("a")) section.setPointA(location);
                else section.setPointB(location);
                break;
        }
        return Result.SUCCESS;
    }

    public Result finishSetup(final Player player) {
        final IIsland island = islandMap.get(player.getUniqueId());
        if (island == null) return Result.DENY;

        final IslandProperties properties = island.getProperties();
        final Point sectionPoint = properties.get("point");
        final TwoSection sectionSelection = (TwoSection) properties.get("selection");
        if (island.hasLocation() && sectionPoint.hasPointA() && sectionSelection.hasPointA() && sectionSelection.hasPointB()) {
            islandService.addIsland(island);
            islandMap.remove(player.getUniqueId());
            return Result.SUCCESS;
        } else return Result.DENY;
    }
}
