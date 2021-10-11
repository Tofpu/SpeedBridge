package me.tofpu.speedbridge.model.object.mode;

import com.google.common.collect.Lists;
import me.tofpu.speedbridge.api.island.mode.Mode;

import java.util.ArrayList;
import java.util.List;

public class ModeImpl implements Mode {
    private final String identifier;
    private final List<Integer> slots;
    private final boolean aDefault;

    public ModeImpl(final String identifier, final boolean aDefault) {
        this(identifier, Lists.newArrayList(), aDefault);
    }

    public ModeImpl(final String identifier, final List<Integer> slots, final boolean aDefault) {
        this.identifier = identifier;
        this.slots = new ArrayList<>(slots);
        this.aDefault = aDefault;
    }

    @Override
    public String identifier() {
        return identifier;
    }

    @Override
    public List<Integer> slots() {
        return slots;
    }

    @Override
    public boolean isDefault() {
        return aDefault;
    }
}
