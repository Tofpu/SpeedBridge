package me.tofpu.speedbridge.game.service.impl;

import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.IGameService;
import me.tofpu.speedbridge.island.IIsland;
import me.tofpu.speedbridge.island.properties.property.TwoSection;
import me.tofpu.speedbridge.island.service.IIslandService;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.service.IUserService;
import me.tofpu.speedbridge.user.timer.Timer;
import me.tofpu.speedbridge.util.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameService implements IGameService {
    private final IIslandService islandService;
    private final IUserService userService;
    private final ILobbyService lobbyService;

    private final Map<UUID, Timer> userTimer = new HashMap<>();
    private final Map<UUID, BukkitTask> userCheck = new HashMap<>();

    public GameService(final IIslandService islandService, final IUserService userService, final ILobbyService lobbyService) {
        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
    }

    @Override
    public Result join(final Player player) {
        if (!lobbyService.hasLobbyLocation()) {
            //TODO: SEND MESSAGE SAYING YOU HAVE TO HAVE A LOBBY LOCATION SET!
            return Result.INVALID_LOBBY;
        }

        final IUser user = userService.getOrDefault(player.getUniqueId());
        if (user.getProperties().getIslandSlot() != null) return Result.DENY;

        final List<IIsland> islands = islandService.getAvailableIslands();
        if (islands.size() < 1) return Result.FULL;

        final IIsland island = islands.get(0);

        user.getProperties().setIslandSlot(island.getSlot());
        island.setTakenBy(user);

        final Inventory inventory = player.getInventory();
        inventory.clear();
        inventory.addItem(new ItemStack(Material.WOOL, 64));

        player.teleport(island.getLocation());

        final TwoSection selection = island.getProperties().get("selection");
        final Cuboid cuboid = new Cuboid(selection.getSectionA(), selection.getSectionB());
        this.userCheck.put(player.getUniqueId(),
                Bukkit.getScheduler()
                        .runTaskTimer(
                                SpeedBridge.getProvidingPlugin(SpeedBridge.class),
                                () -> {
                                    if (!cuboid.isIn(player.getLocation())) {
                                        reset(user);
                                    }
                                },
                                20, 10));

        // TODO: SEND MESSAGE THAT THEY JOINED!

        return Result.SUCCESS;
    }

    @Override
    public Result leave(final Player player) {
        final IUser user = userService.searchForUUID(player.getUniqueId());
        if (user == null) return Result.DENY;
        player.getInventory().clear();

        islandService.resetIsland(user.getProperties().getIslandSlot());
        user.getProperties().setIslandSlot(null);

        userTimer.remove(player.getUniqueId());
        userCheck.get(player.getUniqueId()).cancel();
        userCheck.remove(player.getUniqueId());
        // TODO: TELEPORT PLAYER TO LOBBY!
        player.teleport(lobbyService.getLobbyLocation());
        // TODO: SEND MESSAGE THAT THEY'VE LEFT!

        return Result.SUCCESS;
    }

    @Override
    public boolean isPlaying(final Player player) {
        final IUser user;
        if ((user = userService.searchForUUID(player.getUniqueId())) == null) return false;
        final Integer islandSlot = user.getProperties().getIslandSlot();
        if (islandSlot == null) return false;

        return islandService.getIslandBySlot(islandSlot) != null;
    }

    @Override
    public void addTimer(final IUser user) {
        final Timer timer = new Timer(user.getProperties().getIslandSlot());

        this.userTimer.put(user.getUuid(), timer);
    }

    @Override
    public boolean hasTimer(final IUser user) {
        return this.userTimer.containsKey(user.getUuid());
    }

    @Override
    public void updateTimer(final IUser user) {
        if (user == null) return;

        final UserProperties properties = user.getProperties();
        final Timer gameTimer = userTimer.get(user.getUuid());
        gameTimer.setEnd(System.currentTimeMillis());
        gameTimer.complete();

        final Timer lowestTimer = properties.getTimer();
        if (lowestTimer != null && lowestTimer.getResult() <= gameTimer.getResult()) {
            // TODO: SEND MESSAGE THAT THEY HAVEN'T BEATEN THEIR LOWEST RECORD.

        } else {
            // TODO: SEND MESSAGE THAT THEY HAVE BEATEN THEIR LOWEST RECORD.
            properties.setTimer(gameTimer);
        }

        // TODO: SEND MESSAGE MAYBE?
        reset(user);
    }

    @Override
    public void resetTimer(final IUser user) {
        if (user == null) return;

        userTimer.remove(user.getUuid());
        islandService.resetBlocks(islandService.getIslandBySlot(user.getProperties().getIslandSlot()));
    }

    @Override
    public void reset(final IUser user) {
        if (user == null) return;

        resetTimer(user);

        final Player player = Bukkit.getPlayer(user.getUuid());
        if (player == null) return;
        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(islandService.getIslandBySlot(user.getProperties().getIslandSlot()).getLocation());
    }
}
