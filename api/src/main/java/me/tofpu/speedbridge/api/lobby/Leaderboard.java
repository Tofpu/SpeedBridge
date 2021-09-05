package me.tofpu.speedbridge.api.lobby;

import me.tofpu.speedbridge.api.user.User;
import org.bukkit.plugin.Plugin;

import java.util.List;

// TODO: DUMMY INTERFACE FOR NOW
public interface Leaderboard {
    void start(final Plugin plugin);
    void check(final User user);
    String parse(final int position);
    List<BoardUser> positions();
    void cancel();

    void addAll(final List<BoardUser> list);

    String print();
}
