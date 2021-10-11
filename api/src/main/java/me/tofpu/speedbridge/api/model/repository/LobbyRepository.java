package me.tofpu.speedbridge.api.model.repository;

import me.tofpu.speedbridge.api.misc.Loadable;
import me.tofpu.speedbridge.api.misc.Savable;
import me.tofpu.speedbridge.api.model.object.lobby.Lobby;

public interface LobbyRepository extends Repository, Loadable, Savable {
    Lobby lobby();
}
