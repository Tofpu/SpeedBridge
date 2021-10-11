package me.tofpu.speedbridge.model.repository;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.factory.IslandFactory;
import me.tofpu.speedbridge.api.model.object.island.Island;
import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.IslandRepository;
import me.tofpu.speedbridge.api.model.storage.Storage;

import java.util.*;

public class IslandRepositoryImpl implements IslandRepository {
    private final IslandFactory factory;
    private final Storage storage;

    private final List<Island> islands;
    private final Map<UUID, Island> modifiedIslands;
    private final Deque<Island> revertIslands;

    @Inject
    public IslandRepositoryImpl(final IslandFactory factory, final Storage storage) {
        this.factory = factory;
        this.storage = storage;
        this.islands = new ArrayList<>();
        this.modifiedIslands = new HashMap<>();
        this.revertIslands = new ArrayDeque<>();
    }

    @Override
    public void loadAll() {
        final Collection<Island> islands = storage.loadIslands();
        this.islands.addAll(islands);
    }

    @Override
    public Island register(final Island island) {
        this.islands.add(island);
        return island;
    }

    @Override
    public Island create(final Integer integer) {
        if (integer == null) {
            throw new IllegalStateException("You cannot create an island with" +
                    " null as a slot.");
        }
        return factory.createIsland(integer);
    }

    @Override
    public Optional<Island> remove(final Integer integer) {
        final Optional<Island> optionalIsland = findIslandBy(integer);

        optionalIsland.ifPresent(island -> {
            this.islands.remove(island);
            this.revertIslands.push(island);
            storage.deleteIsland(integer);
        });

        return optionalIsland;
    }

    @Override
    public Optional<Island> findIslandBy(final SearchAlgorithm searchType) {
        switch (searchType.type()) {
            case AVAILABILITY:
            case SLOT:
                return findIslandBy(searchType.slot());
            case USER:
                return findIslandBy(searchType.user());
            case MODE:
                return findIslandBy(searchType.mode());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Island> revert() {
        return revertIslands.isEmpty() ? Optional.empty() : Optional.ofNullable(revertIslands
                .pop());
    }

    private Optional<Island> findIslandBy(final int integer) {
        final boolean available = integer == -1;
        for (final Island island : this.islands) {
            if (available ? island.isAvailable() : integer == island.slot()) {
                return Optional.of(island);
            }
        }
        return Optional.empty();
    }

    private Optional<Island> findIslandBy(final User user) {
        for (final Island island : this.islands) {
            if (island.takenBy().equals(user)) {
                return Optional.of(island);
            }
        }
        return Optional.empty();
    }

    private Optional<Island> findIslandBy(final Mode mode) {
        for (final Island island : this.islands) {
            if (island.mode() != null && island.mode().equals(mode)) {
                return Optional.of(island);
            }
        }
        return Optional.empty();
    }

    @Override
    public void saveAll() {
        storage.saveIslands();
    }

    @Override
    public Collection<Island> islands() {
        return this.islands;
    }
}
