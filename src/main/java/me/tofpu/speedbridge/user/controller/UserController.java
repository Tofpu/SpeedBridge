package me.tofpu.speedbridge.user.controller;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.impl.User;
import me.tofpu.speedbridge.user.service.IUserService;

import java.util.UUID;

public class UserController {
    private final IUserService userService;

    public UserController(@NotNull final IUserService userService) {
        this.userService = userService;
    }

    public void addUser(@NotNull final IUser user) {
        this.userService.addUser(user);
    }

    public void removeUser(@NotNull final IUser user) {
        this.userService.removeUser(user);
    }

    @Nullable
    public IUser searchForUUID(@NotNull final UUID uuid) {
        return this.userService.searchForUUID(uuid);
    }
}
