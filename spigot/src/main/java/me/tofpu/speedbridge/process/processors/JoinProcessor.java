package me.tofpu.speedbridge.process.processors;

import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.process.type.GameProcessor;
import me.tofpu.speedbridge.model.service.GameServiceImpl;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class JoinProcessor extends GameProcessor {
    @Override
    public void process(final GameServiceImpl gameService,
            final Island island, final User user,
            final Player player,
            final ProcessType type) {

        switch (type) {
            case PROCESS:
                // setting the user properties island slot
                // to this island's slot for tracking purposes
                user.properties().islandSlot(island.slot());

                // setting the island takenBy to this user
                // for availability reasons
                island.takenBy(user);

                // teleporting player to the island's location
                // MULTI-WORLD PATCH
                player.teleport(island.location());

                // removing their effects
                player.getActivePotionEffects().clear();

                // setting their gamemode to survival
                player.setGameMode(GameMode.SURVIVAL);
                // resetting their health back to the max health attribute
                player.setHealth(player.getMaxHealth());
                // resetting their food levels back to full
                player.setFoodLevel(20);

                // storing an instance of user & the island that
                // they're in for the runnable to keep track of them
                gameService.gameChecker().put(user, island);

                // if the runnable is paused
                if (gameService.runnable().isPaused()) {
                    // since the runnable is paused
                    // we'll start it
                    gameService.runnable().resume();
                }
                break;
            case REVERSE:
                // resetting the island for the next player
                gameService.resetIsland(user.properties().islandSlot());
                // resetting the user's properties slot to null
                // since they're not playing anymore
                user.properties().islandSlot(null);

                // removing them from our tracker
                gameService.gameTimer().remove(player.getUniqueId());
                gameService.gameChecker().remove(user);

                // if the gameChecker is empty
                if (gameService.gameChecker().isEmpty()) {
                    // since no one is playing right now
                    // we'll stop the runnable to save up resources
                    gameService.runnable().pause();
                }

                // teleporting the player to the lobby location
                player.teleport(gameService.lobbyService().getLobbyLocation());
                break;
        }
        // processing the items to this user
        Process.ITEM_JOIN.process(user, player, type);
    }
}
