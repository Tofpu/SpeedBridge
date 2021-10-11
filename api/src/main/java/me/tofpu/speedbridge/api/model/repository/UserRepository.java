package me.tofpu.speedbridge.api.model.repository;

import me.tofpu.speedbridge.api.misc.*;
import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository extends Repository, Registrable<User, UUID>,
        ManualLoadable<User, UUID>, Findable<User, UUID>,
        ManualSavable<User>, Savable {
    User findOrDefault(final UUID uniqueId);
    Collection<User> users();
}
