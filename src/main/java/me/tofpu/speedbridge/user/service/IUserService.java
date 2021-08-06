package me.tofpu.speedbridge.user.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.user.IUser;

import java.io.File;
import java.util.UUID;

public interface IUserService {
    public IUser createUser(final UUID uuid);

    public void removeUser(final IUser user);

    public IUser getOrDefault(final UUID uuid);

    public IUser searchForUUID(final UUID uuid);

    public void saveAll(final Gson gson, final File directory);

    public IUser load(final Gson gson, final UUID uuid, final File directory);
}
