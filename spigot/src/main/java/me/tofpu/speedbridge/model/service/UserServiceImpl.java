package me.tofpu.speedbridge.model.service;

import com.google.inject.Inject;
import me.tofpu.speedbridge.api.model.object.user.User;
import me.tofpu.speedbridge.api.model.repository.UserRepository;
import me.tofpu.speedbridge.api.model.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Inject
    public UserServiceImpl(final UserRepository repository){
        this.repository = repository;
    }

    @Override
    public User createUser(final UUID uniqueId) {
        return repository.create(uniqueId);
    }

    public Optional<User> load(final UUID uniqueId) {
        return repository.load(uniqueId);
    }

    @Override
    public User findOrDefault(final UUID uniqueId) {
        return repository.findOrDefault(uniqueId);
    }

    @Override
    public Optional<User> find(final UUID uniqueId) {
        return repository.find(uniqueId);
    }

    public void save(final User user) {
        repository.save(user);
    }

    public void saveAll() {
        repository.save();
    }
}
