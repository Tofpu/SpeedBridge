package me.tofpu.speedbridge.api.model.factory;

import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.UUID;

public interface UserFactory {
    User createUser(final UUID uniqueId);
}
