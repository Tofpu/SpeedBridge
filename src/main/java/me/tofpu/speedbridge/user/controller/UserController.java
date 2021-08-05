package me.tofpu.speedbridge.user.controller;

import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.service.IUserService;

import java.util.UUID;

public class UserController {
    private final IUserService userService;

    public UserController(final IUserService userService) {
        this.userService = userService;
    }

    public IUser createUser(final UUID uuid) {
        return this.userService.createUser(uuid);
    }

    public void removeUser(final IUser user) {
        this.userService.removeUser(user);
    }


    public IUser getOrDefault(final UUID uuid) {
        return this.userService.createUser(uuid);
    }


    public IUser searchForUUID(final UUID uuid) {
        return this.userService.searchForUUID(uuid);
    }
}
