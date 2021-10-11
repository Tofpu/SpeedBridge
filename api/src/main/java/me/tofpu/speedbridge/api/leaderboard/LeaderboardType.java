package me.tofpu.speedbridge.api.leaderboard;

public enum LeaderboardType {
    GLOBAL, SESSION;

    public static LeaderboardType match(final String identifier) {
        for (LeaderboardType type : values()){
            if (type.name().equalsIgnoreCase(identifier))
                return type;
        }
        return null;
    }
}
