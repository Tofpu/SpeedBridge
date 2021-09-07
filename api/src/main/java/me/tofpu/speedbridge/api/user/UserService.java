package me.tofpu.speedbridge.api.user;

import java.io.File;
import java.util.UUID;

/**
 * This handles everything that is user related. From creating to getting user instance.
 */
public interface UserService {
    /**
     * Creates a user instance associated with this unique id
     *
     * @param uniqueId the player unique id that you want to create an instance of
     *
     * @return brand new user instance with the associated unique id
     */
    User createUser(final UUID uniqueId);

    /**
     * Removes this user from the user's list
     *
     * @param uniqueId the player unique id that you would want to remove
     */
    void removeUser(final UUID uniqueId);

    /**
     * Removes this user from the user's list
     *
     * @param user the user instance that you would want to remove
     */
    void removeUser(final User user);

    /**
     * Looks up the loaded users associated with this unique id
     *
     * @param uniqueId the unique id of player
     *
     * @return the users instance or a brand new one if not found
     */
    User getOrDefault(final UUID uniqueId, final boolean loadFromFile);

    /**
     * Looks up loaded users associated with this unique id
     *
     * @param uniqueId the unique id of player
     *
     * @return the associated user or null if not found
     */
    User get(final UUID uniqueId);
}
