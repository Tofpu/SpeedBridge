package me.tofpu.speedbridge.process.processors;

import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.model.handler.GameHandler;
import me.tofpu.speedbridge.process.ProcessType;
import me.tofpu.speedbridge.process.type.GameProcessor;
import me.tofpu.speedbridge.process.Process;
import me.tofpu.speedbridge.model.service.GameServiceImpl;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpectatorProcessor extends GameProcessor {
    private final Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();
    private Team team;

    @Override
    public void process(final GameHandler gameHandler,
            final Location location, final ProcessType type,
            final User... users) {
        final User spectator = users[0];
        final User target = users[1];

        final Player spectatorPlayer = spectator.player();
        final Player targetPlayer = target.player();

        if (team == null) {
            team = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam("invisible");
        }

        switch (type) {
            case PROCESS:
                // hiding the spectator
                targetPlayer.hidePlayer(spectatorPlayer);

                // storing the scoreboard the spectator were in (in case it
                // breaks other plugins)
                scoreboardMap.put(spectatorPlayer.getUniqueId(), spectatorPlayer.getScoreboard());
                team.setCanSeeFriendlyInvisibles(true);
                //                team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OWN_TEAM);
                team.addPlayer(spectatorPlayer);
                spectatorPlayer.setScoreboard(team.getScoreboard());

                spectatorPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

                // setting player's properties
                spectatorPlayer.teleport(location);
                spectatorPlayer.setGameMode(GameMode.SURVIVAL);
                spectatorPlayer.setAllowFlight(true);
                spectatorPlayer.setFlying(true);

                // notifying the respective players
                Util.message(spectatorPlayer, Path.MESSAGES_SPECTATING, new String[]{"%player%"}, targetPlayer
                        .getName());

                gameHandler.messageSpectator(target, Util.WordReplacer.replace(Path.MESSAGES_NOTIFY_SPECTATING
                        .getValue(), new String[]{"%player%"},
                        spectatorPlayer.getName()), true);
                break;
            case REVERSE:
                // setting player's properties
                spectatorPlayer.setGameMode(GameMode.SURVIVAL);
                spectatorPlayer.setFlying(false);
                spectatorPlayer.setAllowFlight(false);

                spectatorPlayer.setVelocity(new Vector(0, 0, 0));
                spectatorPlayer.teleport(location);
                // showing the spectator
                spectatorPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);

                spectatorPlayer.setScoreboard(scoreboardMap.remove(spectator.uniqueId()));
                team.removePlayer(spectatorPlayer);

                targetPlayer.showPlayer(spectatorPlayer);

                // notifying the respective players
                Util.message(spectatorPlayer, Path.MESSAGES_NO_LONGER_SPECTATING, new String[]{"%player%"}, targetPlayer.getName());

                gameHandler.messageSpectator(target,
                        Util.WordReplacer.replace(Path.MESSAGES_NOTIFY_NOT_SPECTATING
                        .getValue(), new String[]{"%player%"},
                                spectatorPlayer.getName()), true);
                break;
        }

        // item processor
        Process.ITEM_SPECTATOR.process(spectator, spectatorPlayer, type);
    }
}
