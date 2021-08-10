package me.tofpu.speedbridge.user.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.user.IUser;

import java.io.File;
import java.util.UUID;

public interface IUserService {
    IUser createUser(final UUID uuid);

    void removeUser(final IUser user);

    IUser getOrDefault(final UUID uuid);

    IUser searchForUUID(final UUID uuid);

    void saveAll(final Gson gson, final File directory);

    IUser load(final Gson gson, final UUID uuid, final File directory);
}
