package me.tofpu.speedbridge.game.leaderboard.impl;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;

public class SeasonalLeaderboard extends AbstractLeaderboard {
    public SeasonalLeaderboard(final int capacity) {
        super("Seasonal", capacity, true);
    }

    @Override
    public void check(final User user, final double time) {
        super.check(user, time);
    }
}
