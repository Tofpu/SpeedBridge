package me.tofpu.speedbridge.user.service.impl;

import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.impl.User;
import me.tofpu.speedbridge.user.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService implements IUserService {
    private final List<IUser> users = new ArrayList<>();

    @Override
    public IUser createUser(final UUID uuid){
        final IUser user = new User(uuid);
        this.users.add(user);

        return user;
    }

    @Override
    public void removeUser(final IUser user){
        this.users.remove(user);
    }

    @Override
    public IUser getOrDefault(final UUID uuid){
        IUser user = searchForUUID(uuid);
        if (user == null) createUser(uuid);
        return user;
    }

    @Override
    public IUser searchForUUID(final UUID uuid){
        for (final IUser user : this.users){
            if (user.getUuid() == uuid) return user;
        }
        return null;
    }
}
