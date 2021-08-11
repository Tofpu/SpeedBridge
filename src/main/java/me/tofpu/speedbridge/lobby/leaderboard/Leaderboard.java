package me.tofpu.speedbridge.lobby.leaderboard;

import me.tofpu.speedbridge.lobby.leaderboard.data.BoardUser;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.impl.UserService;
import me.tofpu.speedbridge.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class Leaderboard {
    private final List<BoardUser> mainLeaderboard;
    private final List<BoardUser> cacheLeaderboard;

    private final int limitSize;

    public Leaderboard(final int limitSize){
        this.limitSize = limitSize;

        mainLeaderboard = new ArrayList<>(limitSize);
        cacheLeaderboard = new ArrayList<>(limitSize);
    }
    private ScheduledFuture<?> update;
    private boolean updated;

    public void initialize(){
        update = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (updated){
                updated = false;
                mainLeaderboard.clear();
                mainLeaderboard.addAll(cacheLeaderboard);

                Collections.sort(mainLeaderboard);
            }
        }, 5, 20, TimeUnit.SECONDS);
    }

    public void cancel(){
        this.update.cancel(true);
    }

    public void check(final IUser user){
        final Player player = Bukkit.getPlayer(user.getUuid());
        if (player == null) return;
        check(new BoardUser(player.getName(), user.getUuid(), user.getProperties().getTimer().getResult()));
    }

    public void check(BoardUser against) {
        System.out.println(against.getName());
        final List<BoardUser> updatedList = new ArrayList<>();

        boolean skip = false;
        for (final BoardUser player : getCacheLeaderboard()) {
            if (updatedList.size() >= limitSize) break;
            if (skip) {
                skip = false;
                updatedList.add(player);
                against = player;
            }
            if (player.equals(against)) skip = true;
            System.out.println(player.getName() + " vs " + against.getName());
            System.out.println(player.getResult() + " vs " + against.getResult());
            System.out.println(skip);

            final boolean check = check(player, against);
            System.out.println("isHigher: " + check);
            if (check){
                this.updated = true;

                if (skip){
                    updatedList.remove(player);
                    updatedList.add(against);
                } else {
                    updatedList.add(against);
                    against = player;
                }
            } else{
                updatedList.add(player);
            }
        }

        if (updatedList.size() == 0) {
            this.updated = true;
            updatedList.add(against);
        }

        if (updated){
            this.cacheLeaderboard.clear();
            this.cacheLeaderboard.addAll(updatedList);
        }
    }

    private boolean check(final BoardUser player, final BoardUser against){
        return player.compareTo(against) < 0;
    }

    public void addAll(final List<BoardUser> users){
        for (final BoardUser user : users){
            check(user);
        }
    }

    public String printLeaderboard(){
        final StringBuilder builder = new StringBuilder();
        builder.append("&eLeaderboard");

        int length = 1;
        for (final BoardUser user : getMainLeaderboard()){
            builder.append("\n").append("&e").append(length).append(". ").append(user.getName()).append(" &a(").append(user.getResult()).append(")");
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
