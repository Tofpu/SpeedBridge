package me.tofpu.speedbridge.process.processors;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserProperties;
import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.process.type.GameProcessor;
import me.tofpu.speedbridge.model.service.GameServiceImpl;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.entity.Player;

public class TimerUpdateProcessor extends GameProcessor {
    @Override
    public void process(final GameServiceImpl gameService, final User user, final Player player, final ProcessType type) {
        // if the process type is not process, no need to continue
        if (type != ProcessType.PROCESS) return;

        // getting an instance of timer that were
        // associated with this users unique id
        final Timer gameTimer = gameService.gameTimer().get(user.uniqueId());
        // temporally instance of user's properties for ease of use
        final UserProperties properties = user.properties();
        // temporally instance of user's timer for ease of use
        final Timer lowestTimer = properties.timer();

        // resetting the island state
        gameService.reset(user);

        // ending the timer with the current system millis second
        gameTimer.end(System.currentTimeMillis());

        // messaging this player that they scored
        Util.message(player, Path.MESSAGES_SCORED, new String[]{"%scored%"}, gameTimer.result() + "");

        // notifying the spectators
        gameService.messageSpectator(user,
                Util.WordReplacer.replace(Path.MESSAGES_SPECTATOR_SCORED.getValue(), new String[]{"%player%", "%scored%"}, player.getName(), gameTimer.result() + ""), false);

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

                // messaging this player that they have beaten their score
                Util.message(player, Path.MESSAGES_BEATEN_SCORE, new String[]{"%calu_score%"},
                        result);

                // notifying the target & spectators
                gameService.messageSpectator(user,
                        Util.WordReplacer.replace(Path.MESSAGES_SPECTATOR_BEATEN_SCORE.getValue(), new String[]{"%player%", "%calu_score%"}, player.getName(), result), true);
            }

            // replacing the old record with the player's current record
            properties.timer(gameTimer);

            // manually triggering the leaderboard
            gameService.leaderboardService().check(user, null);
        }
    }
}
