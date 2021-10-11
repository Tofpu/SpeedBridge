package me.tofpu.speedbridge.api.model.object.user;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * A user class associated with the player's unique id.
 */
public interface User {
    /**
     * @return the user unique id
     */
    UUID uniqueId();

    /**
     * @return the user properties
     */
    UserProperties properties();

    /**
     * Player instance of this user.
     *
     * @return a player instance of this user, null if they're offline
     */
    Player player();
}
