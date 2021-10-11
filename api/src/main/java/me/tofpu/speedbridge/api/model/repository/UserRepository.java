package me.tofpu.speedbridge.api.model.repository;

import me.tofpu.speedbridge.api.misc.Findable;
import me.tofpu.speedbridge.api.misc.ManualLoadable;
import me.tofpu.speedbridge.api.misc.Registrable;
import me.tofpu.speedbridge.api.misc.Savable;
import me.tofpu.speedbridge.api.model.object.user.User;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository extends Repository, Registrable<User, UUID>, ManualLoadable<User, UUID>, Findable<User, UUID>, Savable {
    Collection<User> users();
}
