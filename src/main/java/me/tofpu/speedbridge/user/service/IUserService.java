package me.tofpu.speedbridge.user.service;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.user.IUser;

import java.util.UUID;

public interface IUserService {
    public IUser createUser(@NotNull final UUID uuid);

    public void removeUser(@NotNull final IUser user);

    @NotNull
    public IUser getOrDefault(@NotNull final UUID uuid);

    @Nullable
    public IUser searchForUUID(@NotNull final UUID uuid);
}
