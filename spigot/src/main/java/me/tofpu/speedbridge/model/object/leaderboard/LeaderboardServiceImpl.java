package me.tofpu.speedbridge.model.object.leaderboard;

import me.tofpu.speedbridge.api.model.object.leaderboard.Leaderboard;
import me.tofpu.speedbridge.api.model.service.LeaderboardService;
import me.tofpu.speedbridge.api.model.object.leaderboard.LeaderboardType;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.model.object.game.Game;
import me.tofpu.speedbridge.model.object.leaderboard.impl.GlobalLeaderboard;
import me.tofpu.speedbridge.model.object.leaderboard.impl.SessionLeaderboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LeaderboardServiceImpl implements LeaderboardService {
    private final List<AbstractLeaderboard> leaderboards;
    private File directory;

    public LeaderboardServiceImpl() {
        leaderboards = new ArrayList<>();
    }

    public void initialize(final File directory) {
        this.directory = directory;

        final int capacity = Path.LEADERBOARD_SIZE.getValue();
        leaderboards.addAll(Arrays.asList(new GlobalLeaderboard(capacity), new SessionLeaderboard(capacity)));

        Game.EXECUTOR.scheduleWithFixedDelay(this::save, 5, 5, TimeUnit.MINUTES);
    }

    @Override
    public AbstractLeaderboard get(final LeaderboardType type) {
        for (final AbstractLeaderboard leaderboard : leaderboards){
            if (!leaderboard.identifier().equalsIgnoreCase(type.name())) continue;
            return leaderboard;
        }
        return null;
    }

    public void check(final User user, final Predicate<AbstractLeaderboard> filter) {
        for (final AbstractLeaderboard leaderboard : leaderboards){
            if (filter != null && filter.test(leaderboard)) continue;
            leaderboard.check(user);
        }
    }

    @Override
    public void compute(final Predicate<Leaderboard> filter, Consumer<Leaderboard> consumer){
        for (final AbstractLeaderboard leaderboard : leaderboards){
            if (filter != null && filter.test(leaderboard)) continue;
            consumer.accept(leaderboard);
        }
    }

    public void load() {
        for (final File file : directory.listFiles()) {
            final String name = file.getName();

            if (!name.endsWith(".json")) continue;
            try (final FileReader reader = new FileReader(file)) {
                final AbstractLeaderboard leaderboard = DataManager.GSON.fromJson(reader, AbstractLeaderboard.class);

                final LeaderboardType type = LeaderboardType.match(leaderboard.identifier());
                if (type == null) continue;

                get(type).addAll(leaderboard.positions());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        for (final AbstractLeaderboard leaderboard : leaderboards){
            try (final FileWriter writer = new FileWriter(new File(directory, leaderboard.identifier() + ".json"))) {
                writer.write(DataManager.GSON.toJson(leaderboard, AbstractLeaderboard.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File directory() {
        return directory;
    }
}
