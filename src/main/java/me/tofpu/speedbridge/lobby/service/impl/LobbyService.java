package me.tofpu.speedbridge.lobby.service.impl;

import com.google.gson.Gson;
import me.tofpu.speedbridge.lobby.service.ILobbyService;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LobbyService implements ILobbyService {
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
    public void save(final Gson gson, final File file) {
        try (final FileWriter writer = new FileWriter(file)) {
            if (hasLobbyLocation()) writer.write(gson.toJson(getLobbyLocation(), Location.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load(final Gson gson, final File file) {
        try (final FileReader reader = new FileReader(file)) {
            setLobbyLocation(gson.fromJson(reader, Location.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
