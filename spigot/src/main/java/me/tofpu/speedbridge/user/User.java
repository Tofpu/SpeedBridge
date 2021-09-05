package me.tofpu.speedbridge.user;

import me.tofpu.speedbridge.user.properties.UserProperties;

import java.util.UUID;

public interface User {
    UUID uniqueId();
    UserProperties properties();
}
