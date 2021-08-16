package me.tofpu.speedbridge.island.mode;

import com.google.common.collect.Lists;
import me.tofpu.speedbridge.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Mode implements Identifier {
    private final String identifier;
    private final List<Integer> slots;
    private final boolean aDefault;

    public Mode(final String identifier, final boolean aDefault) {
        this(identifier, Lists.newArrayList(), aDefault);
    }

    public Mode(final String identifier, final List<Integer> slots, final boolean aDefault) {
        this.identifier = identifier;
        this.slots = new ArrayList<>(slots);
        this.aDefault = aDefault;
    }

    public Mode(final String identifier, final List<Integer> slots) {
        this(identifier, slots, false);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public boolean isDefault() {
        return aDefault;
    }
}
