package me.tofpu.speedbridge.api.model.service;

import com.google.gson.Gson;
import org.bukkit.Location;

import java.io.File;

public interface LobbyService {
    Location getLobbyLocation();

    void setLobbyLocation(final Location location);

    boolean hasLobbyLocation();

    void save(final Gson gson, final File lobbyFile);

    void load(final Gson gson, final File lobbyFile);
}