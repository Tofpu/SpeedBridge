package me.tofpu.speedbridge.user.impl;

import com.google.common.base.Objects;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.properties.UserProperties;

import java.util.UUID;

public final class UserImpl extends UserProperties implements User {
    private final UUID uuid;

    public UserImpl(final UUID uuid) {
        this(uuid, new UserProperties());
    }

    public UserImpl(final UUID uuid, final UserProperties userProperties) {
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
        return Objects.equal(getUuid(), user.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
