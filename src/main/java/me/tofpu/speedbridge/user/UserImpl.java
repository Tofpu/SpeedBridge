package me.tofpu.speedbridge.user;

import com.google.common.base.Objects;
import me.tofpu.speedbridge.user.properties.UserProperties;
import me.tofpu.speedbridge.user.properties.UserPropertiesFactory;
import me.tofpu.speedbridge.user.properties.UserPropertiesImpl;

import java.util.UUID;

public final class UserImpl extends UserPropertiesImpl implements User {
    private final UUID uuid;

    public UserImpl(final UUID uuid) {
        this(uuid, UserPropertiesFactory.of());
    }

    public UserImpl(final UUID uuid, final UserProperties userProperties) {
        super();
        this.uuid = uuid;
        this.islandSlot(userProperties.islandSlot());
        this.timer(userProperties.timer());
    }

    @Override
    public UUID uniqueId() {
        return uuid;
    }

    @Override
    public UserProperties properties() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(uniqueId(), user.uniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
