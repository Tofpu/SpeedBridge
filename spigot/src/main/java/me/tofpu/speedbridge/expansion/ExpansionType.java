package me.tofpu.speedbridge.expansion;

public enum ExpansionType {
    SCORE("Displays your personal best score"), ISLAND("Displays the island slot you are in"), LIVE_TIMER("Displays a live timer of the game"), LEADERBOARD("Displays data from the leaderboard");

    private final String description;

    ExpansionType(final String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }

    public static ExpansionType match(final String string) {
        for (final ExpansionType stage : ExpansionType.values()) {
            if (stage.name().equalsIgnoreCase(string.toUpperCase().replace("-", "_"))) return stage;
        }
        return null;
    }
}
