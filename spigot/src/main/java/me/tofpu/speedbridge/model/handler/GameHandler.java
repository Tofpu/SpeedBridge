package me.tofpu.speedbridge.model.handler;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.game.Result;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.api.model.service.GameService;
import me.tofpu.speedbridge.api.model.service.IslandService;
import me.tofpu.speedbridge.api.model.service.LobbyService;
import me.tofpu.speedbridge.api.model.service.UserService;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class GameHandler {
    private final LobbyService lobbyService;
    private final IslandService islandService;
    private final UserService userService;

    private final TimerHandler timerHandler;
    private final SpectatorHandler spectatorHandler;

    private final Map<User, Island> players;

    @Inject
    public GameHandler(final LobbyService lobbyService, final IslandService islandService, final UserService userService, final SpectatorHandler spectatorHandler, final TimerHandler timerHandler) {
        this.lobbyService = lobbyService;
        this.islandService = islandService;
        this.userService = userService;
        this.spectatorHandler = spectatorHandler;
        this.timerHandler = timerHandler;

        this.players = new HashMap<>();
    }

    public Result join(final GameService.JoinAlgorithm algorithm,
            final Player player) {
        final User user = userService.findOrDefault(player.getUniqueId());
        if (isPlaying(user) || spectatorHandler.isSpectating(user)) {
            return Result.BUSY;
        }

        final Optional<Island> optionalIsland;
        final Island island;
        switch (algorithm.type()) {
            case RANDOM:
                optionalIsland = islandService.findIslandBy(IslandRepository.SearchAlgorithm.AVAILABILITY);
                break;
            case SELECTIVE:
                optionalIsland = islandService.findIslandBy(IslandRepository.SearchAlgorithm
                        .of(algorithm.slot()));
                break;
            case CATEGORY:
                optionalIsland = islandService.findIslandBy(IslandRepository.SearchAlgorithm
                        .of(algorithm.mode()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + algorithm.type());
        }

        if (!optionalIsland.isPresent()) {
            return Result.INVALID_ISLAND;
        } else if (!(island = optionalIsland.get()).isAvailable()) {
            return Result.FAIL;
        } else {
            Process.GAME_JOIN.process(this, island, user, ProcessType.PROCESS);
            return Result.SUCCESS;
        }
    }

    public Result leave(final Player player) {
        // getting an instance of user associated with this player unique id
        final User user = userService.findOrDefault(player.getUniqueId());
        // true if player is spectating, otherwise false
        final boolean spectating =
                user != null && spectatorHandler.isSpectating(user);
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
            final Optional<User> target =
                    userService.find(spectatorHandler.spectators().get(user).takenBy()
                            .uniqueId());

            if (target.isPresent()) {
                spectatorHandler.leaveSpectate(user);
            }
        } else {
            // looping through the spectators of this user
            final Iterator<Map.Entry<User, Island>> iterator =
                    spectatorHandler.spectators().entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<User, Island> entry = iterator.next();
                if (entry.getValue().slot() != user.properties().islandSlot())
                    continue;
                final User userSpectator = entry.getKey();

                final Player spectator = userSpectator.player();
                if (spectator == null) {
                    iterator.remove();
                    continue;
                }
                // teleporting the spectator back to the lobby
                Process.GAME_SPECTATOR.process(this, lobbyService.lobby()
                        .location()
                        .get(), ProcessType.REVERSE, userSpectator, user);
            }

            // reversing the leaving process for this user
            Process.GAME_JOIN.process(this, null, user, player,
                    ProcessType.REVERSE);

            // sending a message to player that they've left (configurable)
            Util.message(player, Path.MESSAGES_LEFT);
        }
        return Result.SUCCESS;
    }

    public void reset(final User user) {
        // if user instance is null
        if (user == null) {
            // we're returning since no instance of this user exists
            // hence, they're not playing
            return;
        }

        // resetting the player's timer & blocks
        timerHandler.resetTimer(user);

        // getting an instance of player associated with the user unique id
        final Player player = Bukkit.getPlayer(user.uniqueId());
        // checking if the player instance is null
        if (player == null) {
            // this shouldn't happen at all but if it did,
            // it means the player is currently offline
            return;
        }
        final Island island = islandService.findIslandBy(IslandRepository.SearchAlgorithm
                .of(user.properties().islandSlot())).get();

        // setting the player velocity to 0 so they would not
        // fall off whilst the player movement + the teleportation
        player.setVelocity(new Vector(0, 0, 0));
        // teleporting the player back to the island's location
        player.teleport(island.location());
    }

    public void resetIsland(final int slot) {
        // getting an island instance associated with this slot
        final Optional<Island> optionalIsland = this.islandService.findIslandBy(IslandRepository.SearchAlgorithm
                .of(slot));
        if (!optionalIsland.isPresent()) {
            // since the island doesn't exist, we're returning
            return;
        }
        final Island island = optionalIsland.get();

        // resetting the blocks
        resetBlocks(island);
        // making the island available for others to join
        island.takenBy(null);
    }

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

    public boolean isPlaying(final User user) {
        return players.containsKey(user);
    }

    public Map<User, Island> players() {
        return this.players;
    }

    public LobbyService lobbyService() {
        return lobbyService;
    }

    public IslandService islandService() {
        return islandService;
    }

    public UserService userService() {
        return userService;
    }
}
