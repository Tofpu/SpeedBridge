package me.tofpu.speedbridge.api.model.service;

import me.tofpu.speedbridge.api.model.object.lobby.Lobby;

public interface LobbyService {
    void load();
    Lobby lobby();
    void save();
}