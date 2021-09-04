package me.tofpu.speedbridge.game.service;

import com.google.common.collect.Lists;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.game.Result;
import me.tofpu.speedbridge.island.Island;
import me.tofpu.speedbridge.island.mode.Mode;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.island.properties.twosection.TwoSection;
import me.tofpu.speedbridge.island.service.IslandService;
import me.tofpu.speedbridge.lobby.service.LobbyService;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.properties.Timer;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.service.UserService;
import me.tofpu.speedbridge.util.Cuboid;
import me.tofpu.speedbridge.util.Util;
import me.tofpu.speedbridge.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameServiceImpl implements GameService {
    private final Plugin plugin;

    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;

    private final Map<UUID, Timer> gameTimer = new HashMap<>();
    private final Map<UUID, ScheduledFuture<?>> gameTask = new HashMap<>();

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

//        this.gameTask.put(player.getUniqueId(),
//                Bukkit.getScheduler()
//                        .runTaskTimer(plugin,
//                                () -> {
//                                    if (!cuboid.isIn(player.getLocation())) {
//                                        reset(user);
//                                    }
//                                },
//                                20, 10));

        // TODO: MAKE IT SO THERE'D BE A SINGLE SCHEDULER THAT LOOPS THROUGH PLAYERS THAT ARE PLAYING AND CHECK IF THEY'RE OUTSIDE OF THE BORDER, EZ
        this.gameTask.put(player.getUniqueId(), Game.EXECUTOR.scheduleWithFixedDelay(() -> {
            if (!cuboid.isIn(player.getLocation())) {
                reset(user);
            }
        }, 1000, 500, TimeUnit.MILLISECONDS));
        return Result.SUCCESS;
    }

    @Override
    public Result leave(final Player player) {
        final User user = userService.searchForUUID(player.getUniqueId());
        if (user == null) return Result.DENY;
        player.getInventory().clear();

        resetIsland(user.getProperties().getIslandSlot());
        user.getProperties().setIslandSlot(null);

        gameTimer.remove(player.getUniqueId());

        final ScheduledFuture<?> task = gameTask.remove(player.getUniqueId());
        task.cancel(true);

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

        this.gameTimer.put(user.getUuid(), timer);
    }

    @Override
    public boolean hasTimer(final User user) {
        return this.gameTimer.containsKey(user.getUuid());
    }

    @Override
    public Timer getTimer(User user) {
        return gameTimer.get(user.getUuid());
    }

    @Override
    public void updateTimer(final User user) {
        if (user == null) return;
        final Timer gameTimer = this.gameTimer.get(user.getUuid());
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

        gameTimer.remove(user.getUuid());
        resetBlocks(islandService.getIslandBySlot(user.getProperties().getIslandSlot()));
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

    @Override
    public void resetBlocks(final Island island) {
        for (final Location location : island.getPlacedBlocks()) {
            island.getLocation().getWorld().getBlockAt(location).setType(Material.AIR);
        }
        // TODO: CHECK IF THIS ISN'T CAUSING ISSUES
        island.getPlacedBlocks().clear();
    }

    @Override
    public void resetIsland(final int slot) {
        final Island island = this.islandService.getIslandBySlot(slot);
        if (island == null) return;

        resetBlocks(island);
        island.setTakenBy(null);
    }
}
