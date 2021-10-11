package me.tofpu.speedbridge.api.model.service;

import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.Optional;
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
     * Saves this user to a file
     *
     * @param user the user instance that you would want to save
     */
    void save(final User user);

    /**
     * Looks up the loaded users associated with this unique id
     *
     * @param uniqueId the unique id of player
     *
     * @return the users instance, or a brand new one if not found
     */
    User findOrDefault(final UUID uniqueId);

    /**
     * Looks up loaded users associated with this unique id
     *
     * @param uniqueId the unique id of player
     *
     * @return the associated user
     */
    Optional<User> find(final UUID uniqueId);
}
