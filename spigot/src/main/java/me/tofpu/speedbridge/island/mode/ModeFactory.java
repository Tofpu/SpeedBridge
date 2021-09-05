package me.tofpu.speedbridge.island.mode;

import me.tofpu.speedbridge.api.island.mode.Mode;

public class ModeFactory {
    public static Mode of(final String identifier, final boolean isDefault){
        return new ModeImpl(identifier, isDefault);
    }
}
