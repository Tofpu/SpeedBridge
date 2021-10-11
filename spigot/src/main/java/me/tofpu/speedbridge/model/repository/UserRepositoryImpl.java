package me.tofpu.speedbridge.model.repository;

import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.UserRepository;

import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private final List<User> users;

    public UserRepositoryImpl() {
        this.users = new ArrayList<>();
    }

    @Override
    public User create(final UUID uuid) {


        return register(user);
    }

    @Override
    public User register(final User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> load(final UUID uniqueId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> find(final UUID uniqueId) {
        for (final User user : this.users) {
            if (user.uniqueId().equals(uniqueId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> users() {
        return this.users;
    }

    @Override
    public void save() {

    }
}
