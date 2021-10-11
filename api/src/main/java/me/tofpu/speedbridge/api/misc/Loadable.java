package me.tofpu.speedbridge.api.misc;

import java.util.Optional;

public interface Loadable<Object, Type> {
    Optional<Object> load(final Type type);
}
