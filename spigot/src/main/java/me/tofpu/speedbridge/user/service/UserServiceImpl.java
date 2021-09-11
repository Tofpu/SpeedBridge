package me.tofpu.speedbridge.user.service;

import me.tofpu.speedbridge.api.user.User;
import me.tofpu.speedbridge.api.user.UserService;
import me.tofpu.speedbridge.data.DataManager;
import me.tofpu.speedbridge.game.Game;
import me.tofpu.speedbridge.user.UserImpl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserServiceImpl implements UserService {
    private final List<User> users;
    private File directory;

    public UserServiceImpl(){
        this.users = new ArrayList<>();
    }

    public void initialize(final DataManager dataManager){
        this.directory = dataManager.getFiles()[2];

        Game.EXECUTOR.scheduleWithFixedDelay(
                () -> saveAll(false)
                ,5, 5, TimeUnit.MINUTES);
    }

    @Override
    public User createUser(final UUID uuid) {
        final User user = new UserImpl(uuid);
        this.users.add(user);

        return user;
    }

    /**
     * Removes this user from the user's list
     *
     * @param uniqueId the player unique id that you would want to remove
     */
    @Override
    public void removeUser(final UUID uniqueId) {
        this.users.remove(get(uniqueId));
    }

    @Override
    public void removeUser(final User user) {
        if (user == null) return;
        this.users.remove(user);
    }

    @Override
    public User getOrDefault(final UUID uuid, final boolean loadFromCache) {
        User user = get(uuid);
        if (user == null) {
            if (loadFromCache)
                user = load(uuid);

            if (user == null)
                user = createUser(uuid);
        }
        return user;
    }

    @Override
    public User get(final UUID uuid) {
        for (final User user : this.users) {
            if (user.uniqueId().equals(uuid))
                return user;
        }
        return null;
    }

    public void saveAll( final boolean emptyList) {
        if (!this.directory.exists()) this.directory.mkdirs();
        for (final User user : this.users) {
            final File file = new File(this.directory, user.uniqueId().toString() + ".json");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                try (final FileWriter writer = new FileWriter(file)) {
                    writer.write(DataManager.GSON.toJson(user, User.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (emptyList) this.users.clear();
    }

    public void save(final User user) {
        final File file = new File(this.directory, user.uniqueId().toString() + ".json");
        try {
            try (final FileWriter writer = new FileWriter(file)) {
                writer.write(DataManager.GSON.toJson(user, User.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User load(final UUID uuid) {
        final File file = new File(this.directory, uuid.toString() + ".json");
        if (!file.exists()) return null;

        User user = null;
        try {
            user = DataManager.GSON.fromJson(new FileReader(file), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (user == null) return null;

        this.users.add(user);
        return user;
    }

    public File getDirectory() {
        return directory;
    }
}
