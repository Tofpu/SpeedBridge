package me.tofpu.speedbridge.model.object.user;

import com.google.common.base.Objects;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.object.user.UserProperties;
import me.tofpu.speedbridge.model.object.user.properties.UserPropertiesFactory;
import me.tofpu.speedbridge.model.object.user.properties.UserPropertiesImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class UserImpl extends UserPropertiesImpl implements User {
    private final UUID uuid;
    private final Player player;

    public UserImpl(final UUID uuid) {
        this(uuid, UserPropertiesFactory.of());
    }

    public UserImpl(final UUID uuid, final UserProperties userProperties) {
        super();
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);

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

    /**
     * Player instance of this user.
     *
     * @return a player instance of this user, null if they're offline
     */
    @Override
    public Player player() {
        return player;
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
