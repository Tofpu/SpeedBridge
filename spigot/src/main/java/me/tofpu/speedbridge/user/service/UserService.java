package me.tofpu.speedbridge.user.service;

import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.user.User;

import java.io.File;
import java.util.UUID;

public interface UserService {
    void initialize(final DataManager dataManager);

    User createUser(final UUID uuid);

    void removeUser(final User user);

    User getOrDefault(final UUID uuid);

    User searchForUUID(final UUID uuid);

    void saveAll(final File directory, final boolean emptyList);

    void save(final User user, final File directory);

    User load(final UUID uuid, final File directory);
}
