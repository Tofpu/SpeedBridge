package me.tofpu.speedbridge.api.user;

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
}
