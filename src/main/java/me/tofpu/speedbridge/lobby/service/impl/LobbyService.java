package me.tofpu.speedbridge.lobby.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tofpu.speedbridge.lobby.leaderboard.Leaderboard;
import me.tofpu.speedbridge.lobby.leaderboard.data.BoardUser;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LobbyService implements ILobbyService {
    private final Leaderboard leaderboard;

    private Location location;

    public LobbyService() {
        this.leaderboard = new Leaderboard(10);
    }

    @Override
    public Location getLobbyLocation() {
        return location;
    }

    @Override
    public void setLobbyLocation(final Location location) {
        this.location = location;
    }

    @Override
    public boolean hasLobbyLocation() {
        return location != null;
    }

    @Override
    public void save(final Gson gson, final File lobbyFile, final File leaderboardFile) {
        try (final FileWriter writer = new FileWriter(lobbyFile)) {
            if (hasLobbyLocation()) writer.write(gson.toJson(getLobbyLocation(), Location.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (final FileWriter writer = new FileWriter(leaderboardFile)) {
            writer.write(gson.toJson(getLeaderboard().getCacheLeaderboard(), new TypeToken<List<BoardUser>>(){}.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(final Gson gson, final File lobbyFile, final File leaderboardFile) {
        try (final FileReader reader = new FileReader(lobbyFile)) {
            setLobbyLocation(gson.fromJson(reader, Location.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (final FileReader reader = new FileReader(leaderboardFile)) {
            final List<BoardUser> users = gson.fromJson(reader, new TypeToken<List<BoardUser>>(){}.getType());
            if (users == null || users.isEmpty()) return;
            getLeaderboard().addAll(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }
}
