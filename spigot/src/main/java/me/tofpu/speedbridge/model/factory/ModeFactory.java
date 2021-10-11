package me.tofpu.speedbridge.model.factory;

import me.tofpu.speedbridge.api.model.object.mode.Mode;
import me.tofpu.speedbridge.model.object.mode.ModeImpl;

public class ModeFactory {
    public static Mode of(final String identifier, final boolean isDefault){
        return new ModeImpl(identifier, isDefault);
    }
}
