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
import me.tofpu.speedbridge.game.process.ProcessType;
import me.tofpu.speedbridge.game.process.Process;
import me.tofpu.speedbridge.game.runnable.GameRunnable;
import me.tofpu.speedbridge.island.mode.ModeManager;
import me.tofpu.speedbridge.user.properties.timer.TimerFactory;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    private final Map<User, Island> spectators;

    private final GameRunnable runnable;

    public GameServiceImpl(final Plugin plugin, final IslandService islandService, final UserService userService, final LobbyService lobbyService, final LeaderboardServiceImpl leaderboardService) {
        this.islandService = islandService;
        this.userService = userService;
        this.lobbyService = lobbyService;
        this.leaderboardService = leaderboardService;

        this.gameTimer = new HashMap<>();
        this.gameChecker = new HashMap<>();
        this.spectators = new HashMap<>();

        this.runnable = new GameRunnable(plugin, this, this.spectators, this.gameChecker);
    }

    private boolean isPlaying(final User user) {
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

    private boolean isSpectating(final User user) {
        return spectators.containsKey(user);
    }

    private Result join(final Player player, Island island) {
        // if the lobby location were not defined
        if (!lobbyService.hasLobbyLocation()) {
            // since the island lobby isn't defined
            // we're returning INVALID_LOBBY result
            return Result.INVALID_LOBBY;
        }

        // if the player is playing
        if (isPlaying(player) || isSpectating(player)) {
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
        // processing the joining process for this user
        Process.GAME_JOIN.process(this, island, user, player,
                ProcessType.PROCESS);

        // returning SUCCESS result
        return Result.SUCCESS;
    }

    private Result spectate(final User issuer, final User target) {
        // if the userIssuer is already in the spectators list
        final boolean spectate = spectators.containsKey(issuer);

        if (spectate) {
            // reversing the spectating process for this user
            Process.GAME_SPECTATOR.process(this,
                    lobbyService.getLobbyLocation(),
                    ProcessType.REVERSE, issuer, target);

            // remove the spectator from the list
            spectators.remove(issuer);
        } else {
            // Island the target is in
            final Island island = islandService.getIslandBySlot(target.properties()
                    .islandSlot());

            // processing the spectating process with this user
            Process.GAME_SPECTATOR.process(this, island.location(),
                    ProcessType.PROCESS, issuer, target);

            // storing the issuer to the spectators list to keep track of them
            spectators.put(issuer, island);

        }
        return Result.SUCCESS;
    }

    private boolean removeSpectator(final Iterator<?> iterator,
            final Player spectator, final Player target) {
        final User spectatorUser = userService.get(spectator.getUniqueId());
        final User targetUser = userService.get(target.getUniqueId());

        return removeSpectator(iterator, spectatorUser, targetUser);
    }

    private boolean removeSpectator(final Iterator<?> iterator,
            final User spectator, final User target) {
        // if the userIssuer is already in the spectators list
        boolean spectate = spectators.containsKey(spectator);

        if (spectate) {
            // remove them
            iterator.remove();

            // spectator processor
            Process.GAME_SPECTATOR.process(this,
                    lobbyService.getLobbyLocation(), ProcessType.REVERSE, spectator,
                    target);
            return true;
        }
        return false;
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
    public Result spectate(final Player issuer, final Player target) {
        final User userIssuer = userService.get(issuer.getUniqueId());
        final User userTarget = userService.get(target.getUniqueId());

        // if userIssuer is null or is playing
        if (userIssuer == null || isPlaying(issuer)) {
            return Result.FULL;
        }
        // if userTarget is null or is not playing
        if (userTarget == null || !isPlaying(target)) {
            return Result.FAIL;
        }

        return spectate(userIssuer, userTarget);
    }

    @Override
    public Result leave(final Player player) {
        // getting an instance of user associated with this player unique id
        final User user = userService.get(player.getUniqueId());
        // true if player is spectating, otherwise false
        final boolean spectating = user != null && isSpectating(user);
        // true if player is playing, otherwise false
        final boolean playing = user != null && isPlaying(user);

        // if the user instance returned null, or the player isn't playing
        // nor spectating
        if (!spectating & !playing) {
            // since they're definitely not in a game nor spectating
            // returning FAIL result
            return Result.FAIL;
        }

        // if the player is spectating a player
        if (spectating) {
            final User target =
                    userService.get(spectators.get(user).takenBy()
                    .uniqueId());
            spectate(user, target);
        } else {
            // looping through the spectators of this user
            final Iterator<Map.Entry<User, Island>> iterator = spectators.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<User, Island> entry = iterator.next();
                if (entry.getValue().slot() != user.properties().islandSlot())
                    continue;
                final User userSpectator = entry.getKey();

                final Player spectator = Bukkit.getPlayer(userSpectator.uniqueId());
                if (spectator == null) {
                    iterator.remove();
                    continue;
                }
                // teleporting the spectator back to the lobby
                removeSpectator(iterator, spectator, player);
            }

            // reversing the leaving process for this user
            Process.GAME_JOIN.process(this, null, user, player,
                    ProcessType.REVERSE);

            // sending a message to player that they've left (configurable)
            Util.message(player, Path.MESSAGES_LEFT);
        }

        return Result.SUCCESS;
    }

    public void messageSpectator(final User target, final String message,
            boolean includeTarget) {
        // if includeTarget is true, message the target too!
        if (includeTarget) {
            target.player().sendMessage(Util.colorize(message));
        }

        // looping through the spectators of this target
        for (final Map.Entry<User, Island> entry : spectators.entrySet()) {
            if (entry.getValue().slot() != target.properties().islandSlot()) continue;
            final User userSpectator = entry.getKey();

            final Player spectator = userSpectator.player();
            if (spectator == null) {
                continue;
            }

            // messaging the spectator
            spectator.sendMessage(Util.colorize(message));
        }
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

        return isPlaying(user);
    }

    @Override
    public boolean isSpectating(final Player player) {
        final User user;
        // checking if the user instance is null
        if ((user = userService.get(player.getUniqueId())) == null) {
            // since we have no user instance associated with
            // this player unique id, we're returning false
            return false;
        }

        return isSpectating(user);
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

        // notifying the spectators & target
        messageSpectator(user,
                Util.WordReplacer.replace(Path.MESSAGES_SPECTATOR_SCORED.getValue(), new String[]{"%player%", "%scored%"}, player.getName(), gameTimer.result() + ""), true);

        // checking if the player has a personal best record
        // and if the timer was higher than the player's best record
        if (lowestTimer != null && lowestTimer.result() <= gameTimer.result()) {
            // since the timer was higher than the player's best record
            // sending a message saying they've not beaten their best score

            // messaging the player
            Util.message(player, Path.MESSAGES_NOT_BEATEN, new String[]{"%score%"}, lowestTimer.result() + "");
        } else {
            // if the player has personal best score
            if (lowestTimer != null) {
                // since they do, we are calculating the player's best record
                // subtracting with the current timer and sending it to them
                final String result = String.format("%.03f", lowestTimer.result() - gameTimer.result());

                // notifying the target & spectators
                messageSpectator(user,
                        Util.WordReplacer.replace(Path.MESSAGES_SPECTATOR_BEATEN_SCORE.getValue(), new String[]{"%player%", "%calu_score%"}, player.getName(), result), true);
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

    public LobbyService lobbyService() {
        return lobbyService;
    }

    public Map<UUID, Timer> gameTimer() {
        return gameTimer;
    }

    public Map<User, Island> gameChecker() {
        return gameChecker;
    }

    public GameRunnable runnable() {
        return runnable;
    }
}
