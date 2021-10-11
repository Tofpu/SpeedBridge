package me.tofpu.speedbridge.api.misc;

public interface Registrable<Object, Type> {
    Object create(final Type type);
    Object register(final Object object);
}
