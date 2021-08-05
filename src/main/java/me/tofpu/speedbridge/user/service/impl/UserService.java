package me.tofpu.speedbridge.user.service.impl;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.user.IUser;
import me.tofpu.speedbridge.user.impl.User;
import me.tofpu.speedbridge.user.service.IUserService;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService implements IUserService {
    private final List<IUser> users = new ArrayList<>();

    @Override
    public IUser createUser(final UUID uuid) {
        final IUser user = new User(uuid);
        this.users.add(user);

        return user;
    }

    @Override
    public void removeUser(final IUser user) {
        this.users.remove(user);
    }

    @Override
    public IUser getOrDefault(final UUID uuid) {
        IUser user = searchForUUID(uuid);
        if (user == null) createUser(uuid);
        return user;
    }

    @Override
    public IUser searchForUUID(final UUID uuid) {
        for (final IUser user : this.users) {
            if (user.getUuid() == uuid) return user;
        }
        return null;
    }

    @Override
    public void saveAll(final TypeAdapter<IUser> adapter, final File directory) {
        if (!directory.exists()) directory.mkdirs();
        for (final IUser user : this.users){
            final File file = new File(directory, user.getUuid().toString() + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                final JsonWriter writer = new JsonWriter(new FileWriter(file));
                adapter.write(writer, user);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IUser load(final TypeAdapter<IUser> adapter, final UUID uuid, final File directory) {
        final File file = new File(directory, uuid.toString() + ".json");
        if (!file.exists()) return null;

        try {
            return adapter.read(new JsonReader(new FileReader(file)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
