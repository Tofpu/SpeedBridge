package me.tofpu.speedbridge.user.impl;

import com.google.common.base.Objects;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.properties.timer.Timer;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(uuid, user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
