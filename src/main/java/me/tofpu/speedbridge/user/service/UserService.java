package me.tofpu.speedbridge.user.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.user.User;

import java.io.File;
import java.util.UUID;

public interface UserService {
    User createUser(final UUID uuid);

    void removeUser(final User user);

    User getOrDefault(final UUID uuid);

    User searchForUUID(final UUID uuid);

    void saveAll(final Gson gson, final File directory);

    void save(final Gson gson, final User user, final File directory);

    User load(final Gson gson, final UUID uuid, final File directory);
}
