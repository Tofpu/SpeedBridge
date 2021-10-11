package me.tofpu.speedbridge.model.repository;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.factory.UserFactory;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.UserRepository;
import me.tofpu.speedbridge.api.model.storage.Storage;

import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private final UserFactory factory;
    private final Storage storage;
    private final List<User> users;

    @Inject
    public UserRepositoryImpl(final UserFactory factory, final Storage storage) {
        this.factory = factory;
        this.storage = storage;
        this.users = new ArrayList<>();
    }

    @Override
    public User create(final UUID uniqueId) {
        final User user = factory.createUser(uniqueId);
        register(user);
        return user;
    }

    @Override
    public User register(final User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> load(final UUID uniqueId) {
        return storage.loadUser(uniqueId);
    }

    @Override
    public User findOrDefault(final UUID uniqueId) {
        Optional<User> user = find(uniqueId);

        for (int i = 0; i < 2; i++) {
            if (user.isPresent()) {
                break;
            }
            switch (i) {
                case 1:
                    user = load(uniqueId);
                    break;
                case 2:
                    user = Optional.of(create(uniqueId));
                    break;
            }
        }
        return user.get();
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
    public void save(final User user) {
        storage.saveUser(user);
    }

    @Override
    public Collection<User> users() {
        return this.users;
    }

    @Override
    public void save() {
        this.storage.saveUsers();
    }
}
