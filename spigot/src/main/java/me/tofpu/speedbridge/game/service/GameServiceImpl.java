package me.tofpu.speedbridge.game.service;

import com.google.common.collect.Lists;
import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.game.Result;
import me.tofpu.speedbridge.api.island.Island;
import me.tofpu.speedbridge.api.island.IslandService;
import me.tofpu.speedbridge.api.island.mode.Mode;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserProperties;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.game.runnable.GameRunnable;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.user.properties.timer.TimerFactory;
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

public class GameServiceImpl implements GameService {
    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;

    private final Map<UUID, Timer> gameTimer;
    private final Map<User, Island> gameChecker;

    private final GameRunnable runnable;

    public GameServiceImpl(final Plugin plugin, final IslandService islandService, final UserService userService, final LobbyService lobbyService) {
        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;

        this.gameTimer = new HashMap<>();
        this.gameChecker = new HashMap<>();

        this.runnable = new GameRunnable(plugin, this, this.gameChecker);
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
        if (user.properties().islandSlot() != null) return Result.DENY;

        return join(user, islandService.getIslandBySlot(slot));
    }

    @Override
    public Result join(final Player player, Mode mode) {
        if (!lobbyService.hasLobbyLocation()) {
            return Result.INVALID_LOBBY;
        }

        final User user = userService.getOrDefault(player.getUniqueId());
        if (user.properties().islandSlot() != null) return Result.DENY;

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

        user.properties().islandSlot(island.slot());
        island.takenBy(user);

        final Player player = Bukkit.getPlayer(user.uniqueId());
        if (player == null) return Result.DENY;
        player.teleport(island.location());

        final Inventory inventory = player.getInventory();
        inventory.clear();

        player.getActivePotionEffects().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);

        inventory.addItem(new ItemStack(XMaterial.WHITE_WOOL.parseMaterial(), 64));

        if (this.runnable.isPaused()) this.runnable.start();
        gameChecker.put(user, island);

        return Result.SUCCESS;
    }

    @Override
    public Result leave(final Player player) {
        final User user = userService.searchForUUID(player.getUniqueId());
        if (user == null) return Result.DENY;
        player.getInventory().clear();

        resetIsland(user.properties().islandSlot());
        user.properties().islandSlot(null);

        this.gameTimer.remove(player.getUniqueId());
        this.gameChecker.remove(user);

        if (this.gameChecker.isEmpty()) this.runnable.cancel();

        player.teleport(lobbyService.getLobbyLocation());
        Util.message(player, Path.MESSAGES_LEFT);

        return Result.SUCCESS;
    }

    @Override
    public boolean isPlaying(final Player player) {
        final User user;
        if ((user = userService.searchForUUID(player.getUniqueId())) == null) return false;

        final Integer islandSlot = user.properties().islandSlot();
        if (islandSlot == null) return false;

        return islandService.getIslandBySlot(islandSlot) != null;
    }

    @Override
    public void addTimer(final User user) {
        // TODO: CREATE A FACTORY FOR TIMER
        final Timer timer = TimerFactory.of(user.properties().islandSlot());

        this.gameTimer.put(user.uniqueId(), timer);
    }

    @Override
    public boolean hasTimer(final User user) {
        return this.gameTimer.containsKey(user.uniqueId());
    }

    @Override
    public Timer getTimer(User user) {
        return this.gameTimer.get(user.uniqueId());
    }

    @Override
    public void updateTimer(final User user) {
        if (user == null) return;
        final Timer gameTimer = this.gameTimer.get(user.uniqueId());
        reset(user);

        final UserProperties properties = user.properties();
        final Timer lowestTimer = properties.timer();
        final Player player = Bukkit.getPlayer(user.uniqueId());

        gameTimer.end(System.currentTimeMillis());

        Util.message(player, Path.MESSAGES_SCORED, new String[]{"%scored%"}, gameTimer.result() + "");

        if (lowestTimer != null && lowestTimer.result() <= gameTimer.result()) {
            Util.message(player, Path.MESSAGES_NOT_BEATEN, new String[]{"%score%"}, lowestTimer.result() + "");
        } else {
            if (lowestTimer != null) {
                Util.message(player, Path.MESSAGES_BEATEN_SCORE, new String[]{"%calu_score%"}, String.format("%.03f", lowestTimer.result() - gameTimer.result()));
            }

            properties.timer(gameTimer);
            lobbyService.getLeaderboard().check(user);
        }
    }

    @Override
    public void resetTimer(final User user) {
        if (user == null) return;

        this.gameTimer.remove(user.uniqueId());
        resetBlocks(islandService.getIslandBySlot(user.properties().islandSlot()));
    }

    @Override
    public void reset(final User user) {
        if (user == null) return;

        resetTimer(user);

        final Player player = Bukkit.getPlayer(user.uniqueId());
        if (player == null) return;

        player.setVelocity(new Vector(0, 0, 0));
        player.teleport(islandService.getIslandBySlot(user.properties().islandSlot()).location());
    }

    @Override
    public void resetBlocks(final Island island) {
        for (final Location location : island.placedBlocks()) {
            island.location().getWorld().getBlockAt(location).setType(Material.AIR);
        }
        // TODO: CHECK IF THIS ISN'T CAUSING ISSUES
        island.placedBlocks().clear();
    }

    @Override
    public void resetIsland(final int slot) {
        final Island island = this.islandService.getIslandBySlot(slot);
        if (island == null) return;

        resetBlocks(island);
        island.takenBy(null);
    }
}
