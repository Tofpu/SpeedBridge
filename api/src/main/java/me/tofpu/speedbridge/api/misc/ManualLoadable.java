package me.tofpu.speedbridge.api.misc;

import java.util.Optional;

public interface ManualLoadable<Object, Type> {
    Optional<Object> load(final Type type);
}
