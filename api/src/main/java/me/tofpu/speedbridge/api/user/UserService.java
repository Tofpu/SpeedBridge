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
     * @param uniqueId the unique id of player
     *
     * @return brand new user instance with the associated unique id
     */
    User createUser(final UUID uniqueId);

    /**
     * Removes this user from the user's list
     *
     * @param user user instance that you would want to remove
     */
    void removeUser(final User user);

    // TODO: HAVE AN OPTION TO LOAD USER FROM DATA

    /**
     * Looks up the loaded users associated with this unique id
     *
     * @param uniqueId the unique id of player
     *
     * @return the users instance or a brand new one if not found
     */
    User getOrDefault(final UUID uniqueId);

    /**
     * Looks up loaded users associated with this unique id
     *
     * @param uniqueId the unique id of player
     *
     * @return the associated user or null if not found
     */
    // TODO: RENAME THIS METHOD
    User searchForUUID(final UUID uniqueId);

    // TODO: This shouldn't even be here, remove it
    void saveAll(final File directory, final boolean emptyList);

    // TODO: Should this be here, hmm...
    void save(final User user, final File directory);

    // TODO: Should this be here, hmm... maybe?
    User load(final UUID uniqueId, final File directory);
}
