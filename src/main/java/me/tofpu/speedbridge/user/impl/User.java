package me.tofpu.speedbridge.user.impl;

import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.UserProperties;

import java.util.UUID;

public final class User extends UserProperties implements IUser {
    private final UUID uuid;

    public User(final UUID uuid) {
        this(uuid, new UserProperties());
    }

    public User(final UUID uuid, final UserProperties userProperties) {
        super();
        this.uuid = uuid;
        this.setIslandSlot(userProperties.getIslandSlot());
        this.setTimer(userProperties.getTimer());
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public UserProperties getProperties() {
        return this;
    }
}
