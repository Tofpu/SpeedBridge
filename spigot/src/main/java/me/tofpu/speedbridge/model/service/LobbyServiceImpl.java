package me.tofpu.speedbridge.model.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.api.lobby.LobbyService;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LobbyServiceImpl implements LobbyService {
    private Location location;

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
    public void save(final Gson gson, final File lobbyFile) {
        try (final FileWriter writer = new FileWriter(lobbyFile)) {
            if (hasLobbyLocation()) writer.write(gson.toJson(getLobbyLocation(), Location.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(final Gson gson, final File lobbyFile) {
        try (final FileReader reader = new FileReader(lobbyFile)) {
            setLobbyLocation(gson.fromJson(reader, Location.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
