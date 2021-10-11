package me.tofpu.speedbridge.api.misc;

import java.util.Optional;

public interface Findable<Object, Type> {
    Optional<Object> find(final Type type);
}
