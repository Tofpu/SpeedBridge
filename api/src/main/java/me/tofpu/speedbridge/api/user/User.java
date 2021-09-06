package me.tofpu.speedbridge.api.user;

import java.util.UUID;

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
