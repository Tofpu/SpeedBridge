package me.tofpu.speedbridge.game.leaderboard.impl;

import me.tofpu.speedbridge.api.lobby.BoardUser;
import me.tofpu.speedbridge.game.leaderboard.AbstractLeaderboard;
import me.tofpu.speedbridge.util.Util;

public class GlobalLeaderboard extends AbstractLeaderboard {
    public GlobalLeaderboard() {
        super("Global", 10);
    }
}
