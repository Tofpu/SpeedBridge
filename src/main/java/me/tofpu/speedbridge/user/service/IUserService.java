package me.tofpu.speedbridge.user.service;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.user.IUser;

import java.util.UUID;

public interface IUserService {
    public void addUser(@NotNull final IUser user);

    public void removeUser(@NotNull final IUser user);

    @Nullable
    public IUser searchForUUID(@NotNull final UUID uuid);
}
