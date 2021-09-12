package me.tofpu.speedbridge.api.leaderboard;

public interface Leaderboard {
    /**
     * @return the leaderboard identifier
     */
    String identifier();

    /**
     * Fetches the position from this leaderboard to a string.
     *
     * @param position the position that you would like to fetch from the leaderboard
     *
     * @return nicely formatted string position, will return "N/A" if the postiion was filled
     */
    String parse(final int position);

    /**
     * A nicely formatted string from this leaderboard.
     *
     * @return nicely formatted leaderboard
     */
    String print();
}
