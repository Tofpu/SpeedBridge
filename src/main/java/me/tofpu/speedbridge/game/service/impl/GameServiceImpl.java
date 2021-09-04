package me.tofpu.speedbridge.game.service.impl;

import com.google.common.collect.Lists;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.game.result.Result;
import me.tofpu.speedbridge.game.service.GameService;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.mode.manager.ModeManager;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.properties.timer.Timer;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.util.Cuboid;
import me.tofpu.speedbridge.util.Util;
import me.tofpu.speedbridge.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameServiceImpl implements GameService {
    private final Plugin plugin;

    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;

    private final Map<UUID, Timer> userTimer = new HashMap<>();
    private final Map<UUID, BukkitTask> userCheck = new HashMap<>();

    public GameServiceImpl(final Plugin plugin, final IslandService islandService, final UserService userService, final LobbyService lobbyService) {
        this.plugin = plugin;

        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
    }

    @Override
    public Result join(final Player player) {
        return join(player, null);
    }

    @Override
    public Result join(final Player player, final int slot) {
        if (!lobbyService.hasLobbyLocation()) {
            return Result.INVALID_LOBBY;
        }

        final User user = userService.getOrDefault(player.getUniqueId());
        if (user.getProperties().getIslandSlot() != null) return Result.DENY;

        return join(user, islandService.getIslandBySlot(slot));
    }

    @Override
    public Result join(final Player player, Mode mode) {
        if (!lobbyService.hasLobbyLocation()) {
            return Result.INVALID_LOBBY;
        }

        final User user = userService.getOrDefault(player.getUniqueId());
        if (user.getProperties().getIslandSlot() != null) return Result.DENY;

        final List<Island> islands;
        boolean anyIslands = false;

        if (mode == null) {
            // trying to get the default mode defined in the settings
            mode = ModeManager.getModeManager().getDefault();
            // default islands
            final List<Island> defaultIslands = mode == null ? Lists.newArrayList() : islandService.getAvailableIslands(mode);
            islands = defaultIslands.isEmpty() ?
                            islandService.getAvailableIslands() :
                            defaultIslands;

            if (defaultIslands.isEmpty()){
                // that means we grabbed the global available islands
                anyIslands = true;
            }
        } else islands = islandService.getAvailableIslands(mode);
        if (islands.size() < 1) {
            if (!anyIslands) return Result.FULL;
            else return Result.NONE;
        }

        return join(user, islands.get(0));
    }

    @Override
    public Result join(final User user, Island island) {
        if (!lobbyService.hasLobbyLocation()) {
            return Result.INVALID_LOBBY;
        }

        if (island == null) return Result.INVALID_ISLAND;
        else if (!island.isAvailable()) return Result.FULL;

        user.getProperties().setIslandSlot(island.getSlot());
        island.setTakenBy(user);

        final Player player = Bukkit.getPlayer(user.getUuid());
        if (player == null) return Result.DENY;
        player.teleport(island.getLocation());

        final Inventory inventory = player.getInventory();
        inventory.clear();

        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        inventory.addItem(new ItemStack(XMaterial.WHITE_WOOL.parseMaterial(), 64));

        final TwoSection selection = (TwoSection) island.getProperties().get("selection");
        final Cuboid cuboid = new Cuboid(selection.getPointA(), selection.getPointB());

        this.userCheck.put(player.getUniqueId(),
                Bukkit.getScheduler()
                        .runTaskTimer(plugin,
                                () -> {
                                    if (!cuboid.isIn(player.getLocation())) {
                                        reset(user);
                                    }
                                },
                                20, 10));
        return Result.SUCCESS;
    }

    @Override
    public Result leave(final Player player) {
        final User user = userService.searchForUUID(player.getUniqueId());
        if (user == null) return Result.DENY;
        player.getInventory().clear();

        islandService.resetIsland(user.getProperties().getIslandSlot());
        user.getProperties().setIslandSlot(null);

        userTimer.remove(player.getUniqueId());
        userCheck.get(player.getUniqueId()).cancel();
        userCheck.remove(player.getUniqueId());

        player.teleport(lobbyService.getLobbyLocation());
        Util.message(player, Path.MESSAGES_LEFT);

        return Result.SUCCESS;
    }

    @Override
    public boolean isPlaying(final Player player) {
        final User user;
        if ((user = userService.searchForUUID(player.getUniqueId())) == null) return false;
        final Integer islandSlot = user.getProperties().getIslandSlot();
        if (islandSlot == null) return false;

        return islandService.getIslandBySlot(islandSlot) != null;
    }

    @Override
    public void addTimer(final User user) {
        final Timer timer = new Timer(user.getProperties().getIslandSlot());

        this.userTimer.put(user.getUuid(), timer);
    }

    @Override
    public boolean hasTimer(final User user) {
        return this.userTimer.containsKey(user.getUuid());
    }

    @Override
    public Timer getTimer(User user) {
        return userTimer.get(user.getUuid());
    }

    @Override
    public void updateTimer(final User user) {
        if (user == null) return;
        final Timer gameTimer = userTimer.get(user.getUuid());
        reset(user);

        final UserProperties properties = user.getProperties();
        final Timer lowestTimer = properties.getTimer();
        final Player player = Bukkit.getPlayer(user.getUuid());

        gameTimer.setEnd(System.currentTimeMillis());
        gameTimer.complete();

        Util.message(player, Path.MESSAGES_SCORED, new String[]{"%scored%"}, gameTimer.getResult() + "");

        if (lowestTimer != null && lowestTimer.getResult() <= gameTimer.getResult()) {
            Util.message(player, Path.MESSAGES_NOT_BEATEN, new String[]{"%score%"}, lowestTimer.getResult() + "");
        } else {
            if (lowestTimer != null) {
                Util.message(player, Path.MESSAGES_BEATEN_SCORE, new String[]{"%calu_score%"}, String.format("%.03f", lowestTimer.getResult() - gameTimer.getResult()));
            }

            properties.setTimer(gameTimer);
            lobbyService.getLeaderboard().check(user);
        }
    }

    @Override
    public void resetTimer(final User user) {
        if (user == null) return;

        userTimer.remove(user.getUuid());
        islandService.resetBlocks(islandService.getIslandBySlot(user.getProperties().getIslandSlot()));
    }

    @Override
    public void reset(final User user) {
        if (user == null) return;

        resetTimer(user);

        final Player player = Bukkit.getPlayer(user.getUuid());
        if (player == null) return;

        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(islandService.getIslandBySlot(user.getProperties().getIslandSlot()).getLocation());
    }
}
