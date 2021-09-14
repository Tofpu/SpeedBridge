package me.tofpu.speedbridge.game.service;

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
import me.tofpu.speedbridge.game.leaderboard.LeaderboardServiceImpl;
import me.tofpu.speedbridge.game.runnable.GameRunnable;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.user.properties.timer.TimerFactory;
import me.tofpu.speedbridge.util.Util;
import me.tofpu.speedbridge.util.XMaterial;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

public class GameServiceImpl implements GameService {
    private final IslandService islandService;
    private final UserService userService;
    private final LobbyService lobbyService;
    private final LeaderboardServiceImpl leaderboardService;

    private final Map<UUID, Timer> gameTimer;
    private final Map<User, Island> gameChecker;

    private final GameRunnable runnable;

    public GameServiceImpl(final Plugin plugin, final IslandService islandService, final UserService userService, final LobbyService lobbyService, final LeaderboardServiceImpl leaderboardService) {
        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.leaderboardService = leaderboardService;

        this.gameTimer = new HashMap<>();
        this.gameChecker = new HashMap<>();

        this.runnable = new GameRunnable(plugin, this, this.gameChecker);
    }

    private Result join(final Player player, Island island) {
        // if the lobby location were not defined
        if (!lobbyService.hasLobbyLocation()) {
            // since the island lobby isn't defined
            // we're returning INVALID_LOBBY result
            return Result.INVALID_LOBBY;
        }

        // if the player is playing
        if (isPlaying(player)) {
            // since the player is already playing
            // we're returning FAIL result
            return Result.FAIL;
        }

        // getting an instance of user associated with this player unique id
        final User user = userService.getOrDefault(player.getUniqueId(), false);

        // if the island doesn't exist
        if (island == null) {
            // since the island didn't exist
            // we're returning INVALID_ISLAND result
            return Result.INVALID_ISLAND;
        } else if (!island.isAvailable()) {
            // since the island isn't available
            // we're returning FULL result
            return Result.FULL;
        }

        // setting the user properties island slot
        // to this island's slot for tracking purposes
        user.properties().islandSlot(island.slot());

        // setting the island takenBy to this user
        // for availability reasons
        island.takenBy(user);

        // teleporting player to the island's location
        // MULTI-WORLD PATCH
        player.teleport(island.location());

        // getting an instance of the player's inventory
        final Inventory inventory = player.getInventory();

        // clearing their inventory
        inventory.clear();

        // removing their effects
        player.getActivePotionEffects().clear();

        // setting their gamemode to survival
        player.setGameMode(GameMode.SURVIVAL);

        // resetting their health back to the max health attribute
        player.setHealth(player.getMaxHealth());
        // resetting their food levels back to full
        player.setFoodLevel(20);

        // trying to get the material that matches the server version
        final Optional<XMaterial> material = XMaterial.matchXMaterial(Path.SETTINGS_BLOCK.getValue());

        // if the material is present
        if (material.isPresent()){
            // parsing the material chosen
            inventory.addItem(new ItemStack(material.get().parseMaterial(), 64));
        } else {
            // default material
            inventory.addItem(new ItemStack(XMaterial.WHITE_WOOL.parseMaterial(), 64));
        }

        // storing an instance of user & the island that
        // they're in for the runnable to keep track of them
        gameChecker.put(user, island);

        // if the runnable is paused
        if (this.runnable.isPaused()) {
            // since the runnable is paused
            // we'll start it
            this.runnable.resume();
        }

        // returning SUCCESS result
        return Result.SUCCESS;
    }

    @Override
    public Result join(final Player player) {
        return join(player, (Mode) null);
    }

    @Override
    public Result join(final Player player, final int slot) {
        return join(player, islandService.getIslandBySlot(slot));
    }

    @Override
    public Result join(final Player player, Mode mode) {
        // if the lobby location were not defined, (mini-optimization habit)
        if (!lobbyService.hasLobbyLocation()) {
            return Result.INVALID_LOBBY;
        }
        final List<Island> islands = new ArrayList<>();
        boolean defaultMode = false;
        boolean anyIslands = false;

//        // no mode were specified
//        if (mode == null) {
//            // since no mode were specified,
//            // we will try to get the default mode that
//            // was defined in the settings
//            mode = ModeManager.getModeManager().getDefault();
//        }

        // if no mode were specified
        if (mode == null){
            // since no mode were specified,
            // we will try to get the default mode that
            // were defined in the settings
            mode = ModeManager.getModeManager().getDefault();

            // so we can keep track whether we used the original
            // mode or used the default mode
            defaultMode = true;
        }

        // now we're getting the available islands associated with the mode
        final List<Island> defaultIslands = new ArrayList<>(
                islandService.getAvailableIslands(mode)
        );

        // If it's not default mode && the list is empty
        if (defaultMode && defaultIslands.isEmpty()) {
            // since the list were empty
            // we'll look for any other islands that is available right now
            islands.addAll(islandService.getAvailableIslands());

            // this is set to true for message purposes
            anyIslands = true;
        } else if (!defaultIslands.isEmpty()) {
            // since we had an island available associated with
            // the mode we'll add it to the islands list
            islands.addAll(defaultIslands);
        }

        // if the islands list is empty
        if (islands.isEmpty()) {
            if (!anyIslands){
                // meaning that the islands associated
                // with the mode were filled
                return Result.FULL;
            } else {
                // we looked for an alternative
                // and still haven't found any
                // available island
                return Result.NONE;
            }
        }

        return join(player, islands.get(0));
    }

    @Override
    public Result leave(final Player player) {
        // getting an instance of user associated with this player unique id
        final User user = userService.get(player.getUniqueId());

        // if the user instance returned null or the player isn't playing
        if (user == null || !isPlaying(player)) {
            // since they're definitely not in a game
            // returning FAIL result
            return Result.FAIL;
        }
        // cleaning their inventory yet again
        player.getInventory().clear();

        // resetting the island for the next player
        resetIsland(user.properties().islandSlot());
        // resetting the user's properties slot to null
        // since they're not playing anymore
        user.properties().islandSlot(null);

        // removing them from our tracker
        this.gameTimer.remove(player.getUniqueId());
        this.gameChecker.remove(user);

        // if the gameChecker is empty
        if (this.gameChecker.isEmpty()){
            // since no one is playing right now
            // we'll stop the runnable to save up resources
            this.runnable.pause();
        }

        // teleporting the player to the lobby location
        player.teleport(lobbyService.getLobbyLocation());
        // sending a message to player that they've left (configurable)
        Util.message(player, Path.MESSAGES_LEFT);

        return Result.SUCCESS;
    }

    @Override
    public boolean isPlaying(final Player player) {
        final User user;
        // checking if the user instance is null
        if ((user = userService.get(player.getUniqueId())) == null) {
            // since we have no user instance associated with
            // this player unique id, we're returning false
            return false;
        }

        // getting the user island slot
        final Integer islandSlot = user.properties().islandSlot();
        // if the user's island slot is null
        if (islandSlot == null) {
            // since we have no track of the user island slot
            // it probably means they're not in a game
            // returning false
            return false;
        }

        // will return true if the island exists, otherwise false
        return islandService.getIslandBySlot(islandSlot) != null;
    }

    @Override
    public void addTimer(final User user) {
        // creating an instance of timer associated with this user's island slot
        final Timer timer = TimerFactory.of(user.properties().islandSlot());

        // associating the user unique id with the timer & storing it
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
        // if user instance is null
        if (user == null) {
            // we're returning since no instance of this user exists
            // hence, they're not playing
            return;
        }

        // getting an instance of timer that were
        // associated with this users unique id
        final Timer gameTimer = this.gameTimer.get(user.uniqueId());

        // resetting the island state
        reset(user);

        // temporally instance of user's properties for ease of use
        final UserProperties properties = user.properties();
        // temporally instance of user's timer for ease of use
        final Timer lowestTimer = properties.timer();
        // an instance of player associated with this user unique id
        final Player player = Bukkit.getPlayer(user.uniqueId());

        // ending the timer with the current system millis second
        gameTimer.end(System.currentTimeMillis());

        // sending a message to player that they've scored
        Util.message(player, Path.MESSAGES_SCORED, new String[]{"%scored%"}, gameTimer.result() + "");

        // checking if the player has a personal best record
        // and if the timer was higher than the player's best record
        if (lowestTimer != null && lowestTimer.result() <= gameTimer.result()) {
            // since the timer was higher than the player's best record
            // sending a message saying they've not beaten their best score
            Util.message(player, Path.MESSAGES_NOT_BEATEN, new String[]{"%score%"}, lowestTimer.result() + "");
        } else {
            // if the player has personal best score
            if (lowestTimer != null) {
                // since they do, we are calucating the player's best record
                // subtracting with the current timer and sending it to them
                Util.message(player, Path.MESSAGES_BEATEN_SCORE, new String[]{"%calu_score%"}, String.format("%.03f", lowestTimer.result() - gameTimer.result()));
            }

            // replacing the old record with the player's current record
            properties.timer(gameTimer);

            // manually triggering the leaderboard
            leaderboardService.check(user, null);
        }
    }

    @Override
    public void resetTimer(final User user) {
        // if user instance is null
        if (user == null) {
            // we're returning since no instance of this user exists
            // hence, they're not playing
            return;
        }

        // removing the player's cached timer
        this.gameTimer.remove(user.uniqueId());
        // resetting the blocks that the
        // player replaced in the island
        resetBlocks(islandService.getIslandBySlot(user.properties().islandSlot()));
    }

    @Override
    public void reset(final User user) {
        // if user instance is null
        if (user == null) {
            // we're returning since no instance of this user exists
            // hence, they're not playing
            return;
        }

        // resetting the player's timer & blocks
        resetTimer(user);

        // getting an instance of player associated with the user unique id
        final Player player = Bukkit.getPlayer(user.uniqueId());
        // checking if the player instance is null
        if (player == null) {
            // this shouldn't happen at all but if it did,
            // it means the player is currently offline
            return;
        }

        // setting the player velocity to 0 so they would not
        // fall off whilst the player movement + the teleportation
        player.setVelocity(new Vector(0, 0, 0));
        // teleporting the player back to the island's location
        player.teleport(islandService.getIslandBySlot(user.properties().islandSlot()).location());
    }

    @Override
    public void resetBlocks(final Island island) {
        // looping through the placed blocks
        for (final Location location : island.placedBlocks()) {
            // getting the block instance associated with
            // the block location and setting the type to air
            island.location().getWorld().getBlockAt(location).setType(Material.AIR);
        }
        // clearing the placedBlocks list to prevent memory leaks
        island.placedBlocks().clear();
    }

    @Override
    public void resetIsland(final int slot) {
        // getting an island instance associated with this slot
        final Island island = this.islandService.getIslandBySlot(slot);
        if (island == null) {
            // since the island doesn't exist, we're returning
            return;
        }

        // resetting the blocks
        resetBlocks(island);
        // making the island available for others to join
        island.takenBy(null);
    }
}
