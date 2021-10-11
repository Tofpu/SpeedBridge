package me.tofpu.speedbridge.api.model.service;


import java.util.function.Consumer;
import java.util.function.Predicate;

public interface LeaderboardService {
    /**
     * Retrieves a leaderboard that associates with the type.
     *
     * @param type the leaderboard type
     *
     * @return the leaderboard instance that associates with the type, otherwise null.
     */
    Leaderboard get(final LeaderboardType type);

    /**
     * This method allows you to run a method across all the leaderboard within a single method, so long it wasn't filtered.
     *
     * @param filter filter
     * @param consumer the consumer value
     */
    void compute(final Predicate<Leaderboard> filter, Consumer<Leaderboard> consumer);
}
