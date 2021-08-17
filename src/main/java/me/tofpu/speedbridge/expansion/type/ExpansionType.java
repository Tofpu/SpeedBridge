package me.tofpu.speedbridge.expansion.type;

public enum ExpansionType {
    SCORE, ISLAND, LIVE_TIMER, LEADERBOARD;

    public static ExpansionType getMatch(final String string) {
        for (final ExpansionType stage : ExpansionType.values()) {
            if (stage.name().equalsIgnoreCase(string.toUpperCase().replace("-", "_"))) return stage;
        }
        return null;
    }
}
