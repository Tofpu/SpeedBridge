package me.tofpu.speedbridge.game.leaderboard.impl;

import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;

public class SessionLeaderboard extends AbstractLeaderboard {
    public SessionLeaderboard(final int capacity) {
        super("Session", capacity);
    }
}
