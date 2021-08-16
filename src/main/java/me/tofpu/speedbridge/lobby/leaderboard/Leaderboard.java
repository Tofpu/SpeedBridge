package me.tofpu.speedbridge.lobby.leaderboard;

import me.tofpu.speedbridge.lobby.leaderboard.data.BoardUser;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.properties.timer.Timer;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    private final List<BoardUser> mainLeaderboard;
    private final List<BoardUser> cacheLeaderboard;

    private final int limitSize;
    private ScheduledFuture<?> update;
    private boolean updated;

    public Leaderboard(final int limitSize) {
        this.limitSize = limitSize;

        mainLeaderboard = new ArrayList<>(limitSize);
        cacheLeaderboard = new ArrayList<>(limitSize);
    }

    public void initialize() {
        update = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            mainLeaderboard.clear();
            mainLeaderboard.addAll(sortGet());
//            if (updated){
//                updated = false;
//                mainLeaderboard.clear();
//                mainLeaderboard.addAll(sortGet());
//
////                mainLeaderboard.sort(BoardUser::compareTo);
//            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    public void cancel() {
        this.update.cancel(true);
    }

    public void check(final User user) {
        final Player player = Bukkit.getPlayer(user.getUuid());
        if (player == null) return;
        final Timer timer = user.getProperties().getTimer();

        BoardUser boardUser = get(user.getUuid());
        if (boardUser == null) {
            boardUser = new BoardUser(player.getName(), user.getUuid(), timer == null ? null : timer.getResult());
        } else {
            if (boardUser.getScore() == null || boardUser.getScore() > timer.getResult()) {
                boardUser.setScore(timer.getResult());
            }
        }
        add(boardUser);
    }

    public List<BoardUser> sortGet() {
        final List<BoardUser> players = new ArrayList<>(getCacheLeaderboard());

        int max;
        for (int i = 0; i < players.size(); i++) {
            max = i;
            BoardUser playerMax = players.get(max);
            BoardUser playerJ;

            for (int j = i; j < players.size(); j++) {
                playerJ = players.get(j);

                if (playerJ.getScore() < playerMax.getScore()) {
                    max = j;
                    playerMax = players.get(max);
                }
            }
            //Swap biggest and current locations
            final BoardUser placeholder = players.get(i);
            players.set(i, playerMax);
            if (!placeholder.equals(playerMax)) players.set(max, placeholder);
        }

//        final List<BoardUser> sort = new ArrayList<>(new HashSet<>(players));
//        sort.sort(BoardUser::compareTo);
        return players;
    }

    public BoardUser get(final UUID uuid) {
        for (final BoardUser user : getCacheLeaderboard()) {
            if (user.getUuid().equals(uuid)) return user;
        }
        return null;
    }

    public void add(final BoardUser boardUser) {
        for (final BoardUser user : cacheLeaderboard) {
            if (user.equals(boardUser)) {
//                System.out.println(user.getName() + " equals " + boardUser.getName());
                if (user.getScore() > boardUser.getScore()) {
                    user.setScore(boardUser.getScore());
                }
                return;
            }
        }
        cacheLeaderboard.add(boardUser);
    }

    public void addAll(final List<BoardUser> users) {
        for (final BoardUser user : users) {
            add(user);
        }
//        cacheLeaderboard.addAll(users);
    }

    public String printLeaderboard() {
        final StringBuilder builder = new StringBuilder();
        builder.append("&eLeaderboard");

        int length = 1;
        for (final BoardUser user : getMainLeaderboard()) {
            builder.append("\n").append("&e").append(length).append(". ").append(user.getName()).append(" &a(").append(user.getScore()).append(")");
            length++;
        }

        return Util.colorize(builder.toString());
    }

    public boolean isUpdated() {
        return updated;
    }

    public List<BoardUser> getMainLeaderboard() {
        return new ArrayList<>(mainLeaderboard);
    }

    public List<BoardUser> getCacheLeaderboard() {
        return new ArrayList<>(cacheLeaderboard);
    }
}
