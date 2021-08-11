package me.tofpu.speedbridge.lobby.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.lobby.leaderboard.Leaderboard;
import org.bukkit.Location;

import java.io.File;

public interface ILobbyService {
    Location getLobbyLocation();

    void setLobbyLocation(final Location location);

    boolean hasLobbyLocation();

    void save(final Gson gson, final File lobbyFile, final File leaderboardFile);

    void load(final Gson gson, final File lobbyFile, final File leaderboardFile);

    public Leaderboard getLeaderboard();
}
