package me.tofpu.speedbridge.api.misc;

import java.util.Optional;

public interface Removable<Object, Type> {
    Optional<Object> remove(final Type type);
}
