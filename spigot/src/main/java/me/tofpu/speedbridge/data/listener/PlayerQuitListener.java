package me.tofpu.speedbridge.data.listener;

import me.tofpu.speedbridge.api.game.GameService;
import me.tofpu.speedbridge.api.leaderboard.LeaderboardType;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.leaderboard.LeaderboardServiceImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final UserService userService;
    private final GameService gameService;
    private final LeaderboardServiceImpl leaderboardService;
    private final DataManager dataManager;

    public PlayerQuitListener(final UserService userService, final GameService gameService, final LeaderboardServiceImpl leaderboardService, final DataManager dataManager) {
        this.userService = userService;
        this.gameService = gameService;
        this.leaderboardService = leaderboardService;
        this.dataManager = dataManager;
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final boolean playing = gameService.isPlaying(player);

        final User user = userService.get(player.getUniqueId());
        if (playing || gameService.isSpectating(player)) {
            gameService.leave(player);
        }

        leaderboardService.get(LeaderboardType.SEASONAL).remove(user);
        dataManager.unloadUser(event.getPlayer().getUniqueId());
    }
}
