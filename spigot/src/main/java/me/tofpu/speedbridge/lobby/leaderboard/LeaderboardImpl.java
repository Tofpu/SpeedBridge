package me.tofpu.speedbridge.lobby.leaderboard;

import com.google.common.collect.Lists;
import me.tofpu.speedbridge.api.lobby.Leaderboard;
import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class LeaderboardImpl implements Leaderboard {
    private final List<BoardUserImpl> mainLeaderboard;
    private final List<BoardUserImpl> cacheLeaderboard;

    private final int limitSize;
    private BukkitTask update;

    public LeaderboardImpl(final int limitSize) {
        this.limitSize = limitSize;

        mainLeaderboard = new ArrayList<>(limitSize);
        cacheLeaderboard = new ArrayList<>(limitSize);
    }

    @Override
    public void start(final Plugin plugin) {
        update = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            mainLeaderboard.clear();
            final List<BoardUserImpl> users = sortGet();
            mainLeaderboard.addAll(users.subList(0, Math.min(users.size(), this.limitSize)));
        }, 0, 20 * 10);
    }

    @Override
    public void cancel() {
        this.update.cancel();
    }

    @Override
    public void check(final User user) {
        final Player player = Bukkit.getPlayer(user.uniqueId());
        if (player == null) return;
        final Timer timer = user.properties().timer();

        BoardUserImpl boardUser = get(user.uniqueId());
        if (boardUser == null) {
            boardUser = new BoardUserImpl(player.getName(), user.uniqueId(), timer == null ? null : timer.result());
        } else {
            if (boardUser.score() == null || boardUser.score() > timer.result()) {
                boardUser.score(timer.result());
            }
        }
        add(boardUser);
    }

    private List<BoardUserImpl> sortGet() {
        final List<BoardUserImpl> players = new ArrayList<>(getCacheLeaderboard());
        if (players.isEmpty()) return players;

        int max;
        for (int i = 0; i < players.size(); i++) {
            max = i;
            BoardUserImpl playerMax = players.get(max);
            BoardUserImpl playerJ;

            for (int j = i; j < players.size(); j++) {
                playerJ = players.get(j);
                if (playerJ.score() < playerMax.score()) {
                    max = j;
                    playerMax = players.get(max);
                }
            }
            //Swap biggest and current locations
            final BoardUserImpl placeholder = players.get(i);
            players.set(i, playerMax);
            if (!placeholder.equals(playerMax)) players.set(max, placeholder);
        }

        return players;
    }

    public BoardUserImpl get(final UUID uuid) {
        for (final BoardUserImpl user : getCacheLeaderboard()) {
            if (user.uniqueId().equals(uuid)) return user;
        }
        return null;
    }

    public void add(final BoardUserImpl boardUser) {
        if (boardUser == null || boardUser.score() == null) return;
        for (final BoardUserImpl user : getCacheLeaderboard()) {
            if (user.equals(boardUser)) {
                if (user.score() > boardUser.score()) {
                    user.score(boardUser.score());
                }
                return;
            }
        }
        cacheLeaderboard.add(boardUser);
    }

    @Override
    public void addAll(final List<BoardUserImpl> users) {
        for (final BoardUserImpl user : users) {
            add(user);
        }
    }

    @Override
    public String print() {
        final StringBuilder builder = new StringBuilder();
        builder.append("&eLeaderboard");

        int length = 1;
        for (final BoardUserImpl user : getMainLeaderboard()) {
            builder.append("\n").append("&e").append(length).append(". ").append(user.name()).append(" &a(").append(user.score()).append(")");
            length++;
        }

        return Util.colorize(builder.toString());
    }

    @Override
    public String parse(final int slot){
        if (getMainLeaderboard().size() <= slot) return "N/A";
        final BoardUserImpl user = getMainLeaderboard().get(slot);
        return user == null ? "N/A" : Util.colorize(user.name() + " &a(" + user.score() + ")");
    }

    public List<BoardUserImpl> getMainLeaderboard() {
        return Lists.newArrayList(mainLeaderboard);
    }

    public List<BoardUserImpl> getCacheLeaderboard() {
        return Lists.newArrayList(cacheLeaderboard);
    }
}
