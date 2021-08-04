package me.tofpu.speedbridge.user;

import com.sun.istack.internal.NotNull;
import me.tofpu.speedbridge.user.properties.UserProperties;

import java.util.UUID;

public interface IUser {
    @NotNull
    public UUID getUuid();

    @NotNull
    public UserProperties getProperties();
}
