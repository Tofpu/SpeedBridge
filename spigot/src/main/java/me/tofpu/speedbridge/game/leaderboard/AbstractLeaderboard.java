package me.tofpu.speedbridge.game.leaderboard;

import me.tofpu.speedbridge.api.lobby.BoardUser;
import me.tofpu.speedbridge.api.leaderboard.Leaderboard;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractLeaderboard implements Leaderboard {
    protected final List<BoardUser> leaderboard;

    private final String identifier;
    private final int capacity;
    private final boolean temporally;
    private BukkitTask bukkitTask;

    public AbstractLeaderboard(final String identifier, final int capacity) {
        this.identifier = identifier;
        this.capacity = capacity;
        this.temporally = false;
        this.leaderboard = new ArrayList<>(capacity);
    }

    public AbstractLeaderboard(final String identifier, final int capacity,
            final boolean temporally) {
        this.identifier = identifier;
        this.capacity = capacity;
        this.temporally = temporally;
        this.leaderboard = new ArrayList<>(capacity);
    }

    private List<BoardUser> sortGet() {
        final List<BoardUser> leaderboard = new ArrayList<>(this.leaderboard);
        if (leaderboard.isEmpty()) return leaderboard;

        int max;
        for (int i = 0; i < leaderboard.size(); i++) {
            max = i;
            BoardUser playerMax = leaderboard.get(max);
            BoardUser playerJ;

            for (int j = i; j < leaderboard.size(); j++) {
                playerJ = leaderboard.get(j);
                if (playerJ.score() < playerMax.score()) {
                    max = j;
                    playerMax = leaderboard.get(max);
                }
            }
            //Swap biggest and current locations
            final BoardUser placeholder = leaderboard.get(i);
            leaderboard.set(i, playerMax);
            if (!placeholder.equals(playerMax)) leaderboard.set(max, placeholder);
        }

        return leaderboard;
    }

    public void start(Plugin plugin) {
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            final List<BoardUser> users = sortGet();
            leaderboard.clear();
            leaderboard.addAll(users.subList(0, Math.min(users.size(), capacity)));
        }, 0, 20 * 10);
    }

    public void cancel() {
        bukkitTask.cancel();
    }

    public BoardUser get(final UUID uniqueId) {
        for (final BoardUser user : leaderboard) {
            if (user.uniqueId().equals(uniqueId))
                return user;
        }
        return null;
    }

    public void add(final BoardUser boardUser){
        if (boardUser == null || boardUser.score() == null) return;
        for (final BoardUser user : leaderboard) {
            if (user.equals(boardUser)) {
                if (user.score() > boardUser.score()) {
                    user.score(boardUser.score());
                }
                return;
            }
        }
        leaderboard.add(boardUser);
    }

    public void addAll(final List<BoardUser> users) {
        for (final BoardUser user : users) {
            add(user);
        }
    }

    public void check(final User user, final double time) {
        final Player player = Bukkit.getPlayer(user.uniqueId());
        final Timer timer = user.properties().timer();
        if (player == null || timer == null) return;

        BoardUser boardUser = get(user.uniqueId());
        if (boardUser == null) {
            boardUser =
                    new BoardUserImpl.Builder().name(player.getName()).uniqueId(user.uniqueId()).result(timer == null ? null : time).build();
        } else {
            if (boardUser.score() == null || boardUser.score() > time) {
                boardUser.score(time);
            }
        }
        add(boardUser);
    }

    public void remove(final User user) {
        final BoardUser boardUser = get(user.uniqueId());
        if (boardUser == null) return;
        leaderboard.remove(boardUser);
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public String print(){
        final StringBuilder builder = new StringBuilder();
        builder.append(Path.LEADERBOARD_HEADER.getValue());

        for (int i = 0; i < positions().size(); i++) {
            if (builder.capacity() != 1) builder.append("\n");
            builder.append(parse(i));
        }

        return Util.colorize(builder.toString());
    }

    @Override
    public String parse(final int slot){
        if (positions().size() <= slot) return "N/A";
        final int position = slot + 1;
        final BoardUser user = positions().get(slot);

        return user == null ? "N/A" : Util.colorize(Util.WordReplacer.replace(Path.LEADERBOARD_STYLE.getValue(), new String[]{"{position}", "{name}", "{score}"}, position + "", user.name(), user.score() + ""));
    }

    public Optional<Double> parseScore(final UUID uniqueId) {
        final BoardUser user = get(uniqueId);
        if (user == null) return Optional.empty();
        final Double score = user.score();

        return Optional.ofNullable(score);
    }

    public List<BoardUser> positions() {
        return leaderboard;
    }

    public int capacity() {
        return capacity;
    }

    public boolean isTemporally() {
        return temporally;
    }
}
