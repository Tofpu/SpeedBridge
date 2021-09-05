package me.tofpu.speedbridge.api.user;

import java.io.File;
import java.util.UUID;

public interface UserService {
    User createUser(final UUID uuid);

    void removeUser(final User user);

    User getOrDefault(final UUID uuid);

    User searchForUUID(final UUID uuid);

    void saveAll(final File directory, final boolean emptyList);

    void save(final User user, final File directory);

    User load(final UUID uuid, final File directory);
}
