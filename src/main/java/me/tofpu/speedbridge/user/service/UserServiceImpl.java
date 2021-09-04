package me.tofpu.speedbridge.user.service;

import com.google.gson.Gson;
import me.tofpu.speedbridge.user.User;
import me.tofpu.speedbridge.user.UserImpl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final List<User> users;

    public UserServiceImpl(){
        users = new ArrayList<>();
    }

    @Override
    public User createUser(final UUID uuid) {
        final User user = new UserImpl(uuid);
        this.users.add(user);

        return user;
    }

    @Override
    public void removeUser(final User user) {
        this.users.remove(user);
    }

    @Override
    public User getOrDefault(final UUID uuid) {
        User user = searchForUUID(uuid);
        if (user == null) user = createUser(uuid);
        return user;
    }

    @Override
    public User searchForUUID(final UUID uuid) {
        for (final User user : this.users) {
            if (user.getUuid().equals(uuid)) return user;
        }
        return null;
    }

    @Override
    public void saveAll(final Gson gson, final File directory) {
        if (!directory.exists()) directory.mkdirs();
        for (final User user : this.users) {
            final File file = new File(directory, user.getUuid().toString() + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                try (final FileWriter writer = new FileWriter(file)) {
                    writer.write(gson.toJson(user, User.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(final Gson gson, final User user, final File directory) {
        final File file = new File(directory, user.getUuid().toString() + ".json");
        try {
            try (final FileWriter writer = new FileWriter(file)) {
                writer.write(gson.toJson(user, User.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User load(final Gson gson, final UUID uuid, final File directory) {
        final File file = new File(directory, uuid.toString() + ".json");
        if (!file.exists()) return null;

        User user = null;
        try {
            user = gson.fromJson(new FileReader(file), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user == null) return null;

        this.users.add(user);
        return user;
    }
}
