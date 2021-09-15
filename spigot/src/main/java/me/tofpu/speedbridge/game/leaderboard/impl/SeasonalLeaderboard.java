package me.tofpu.speedbridge.game.leaderboard.impl;

import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;

public class SeasonalLeaderboard extends AbstractLeaderboard {
    public SeasonalLeaderboard(final int capacity) {
        super("Seasonal", capacity);
    }
}
