package me.tofpu.speedbridge.island.mode;

public class ModeFactory {
    public static Mode of(final String identifier, final boolean isDefault){
        return new ModeImpl(identifier, isDefault);
    }
}
