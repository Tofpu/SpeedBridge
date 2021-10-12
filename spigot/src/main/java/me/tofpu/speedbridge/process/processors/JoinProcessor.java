package me.tofpu.speedbridge.process.processors;

import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.model.handler.GameHandler;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.process.type.GameProcessor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class JoinProcessor extends GameProcessor {
    @Override
    public void process(final GameHandler gameHandler,
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
                gameHandler.gameChecker().put(user, island);

                // if the runnable is paused
                if (gameHandler.runnable().isPaused()) {
                    // since the runnable is paused
                    // we'll start it
                    gameHandler.runnable().resume();
                }
                break;
            case REVERSE:
                // resetting the island for the next player
                gameHandler.resetIsland(user.properties().islandSlot());
                // resetting the user's properties slot to null
                // since they're not playing anymore
                user.properties().islandSlot(null);

                // removing them from our tracker
                gameHandler.gameTimer().remove(player.getUniqueId());
                gameHandler.gameChecker().remove(user);

                // if the gameChecker is empty
                if (gameHandler.gameChecker().isEmpty()) {
                    // since no one is playing right now
                    // we'll stop the runnable to save up resources
                    gameHandler.runnable().pause();
                }

                // teleporting the player to the lobby location
                gameHandler.lobbyService().lobby().teleportToLobby(user);
                break;
        }
        // processing the items to this user
        Process.ITEM_JOIN.process(user, player, type);
    }
}
