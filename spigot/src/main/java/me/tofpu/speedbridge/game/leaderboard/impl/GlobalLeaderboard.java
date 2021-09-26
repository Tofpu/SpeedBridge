package me.tofpu.speedbridge.game.leaderboard.impl;

import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;

public class GlobalLeaderboard extends AbstractLeaderboard {
    public GlobalLeaderboard(final int capacity) {
        super("Global", capacity);
    }
}
