package me.tofpu.speedbridge.model.object.leaderboard.impl;

import me.tofpu.speedbridge.model.object.leaderboard.AbstractLeaderboard;

public class SessionLeaderboard extends AbstractLeaderboard {
    public SessionLeaderboard(final int capacity) {
        super("Session", capacity);
    }
}
