package me.tofpu.speedbridge.api.user;

import java.util.UUID;

public interface User {
    UUID uniqueId();
    UserProperties properties();
}
