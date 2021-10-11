package me.tofpu.speedbridge.api.model.repository;

import me.tofpu.speedbridge.api.misc.Loadable;
import me.tofpu.speedbridge.api.misc.Registrable;
import me.tofpu.speedbridge.api.misc.Removable;
import me.tofpu.speedbridge.api.misc.Savable;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.Collection;
import java.util.Optional;

public interface IslandRepository extends Repository, Loadable,
        Registrable<Island,
        Integer>, Removable<Island, Integer>, Savable {
    Optional<Island> findIslandBy(final SearchAlgorithm type);
    Optional<Island> revert();
    Collection<Island> islands();

    enum SearchType {
        // IslandRepository#findIslandBy(SearchType.AVAILABILITY, 10);
        AVAILABILITY, SLOT, USER, MODE
    }

    class SearchAlgorithm {
        private final SearchType type;
        private int slot;
        private User user;
        private Mode mode;

        public static SearchAlgorithm of(final int slot) {
            return new SearchAlgorithm(slot);
        }

        public static SearchAlgorithm of(final User user) {
            return new SearchAlgorithm(user);
        }

        public static SearchAlgorithm of(final Mode mode) {
            return new SearchAlgorithm(mode);
        }

        private SearchAlgorithm(final int slot) {
            this.type = slot == -1 ? SearchType.AVAILABILITY : SearchType.SLOT;
            this.slot = slot;
        }

        private SearchAlgorithm(final User user) {
            this.type = SearchType.USER;
            this.user = user;
        }

        private SearchAlgorithm(final Mode mode) {
            this.type = SearchType.MODE;
            this.mode = mode;
        }

        public SearchType type() {
            return type;
        }

        public int slot() {
            return slot;
        }

        public User user() {
            return user;
        }

        public Mode mode() {
            return mode;
        }
    }
}
