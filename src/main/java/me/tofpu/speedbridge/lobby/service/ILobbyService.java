package me.tofpu.speedbridge.lobby.service;

import com.google.gson.Gson;
import org.bukkit.Location;

import java.io.File;

public interface ILobbyService {
    public Location getLobbyLocation();

    public void setLobbyLocation(final Location location);

    public boolean hasLobbyLocation();

    public void save(final Gson gson, final File file);

    public void load(final Gson gson, final File file);
}
