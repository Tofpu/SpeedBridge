package me.tofpu.speedbridge.api.lobby;

import java.util.UUID;

public interface BoardUser {
    String name();
    UUID uniqueId();

    Double score();
    BoardUser score(Double score);
}
