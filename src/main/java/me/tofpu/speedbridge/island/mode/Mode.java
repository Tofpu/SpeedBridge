package me.tofpu.speedbridge.island.mode;

import java.util.ArrayList;
import java.util.List;

public class Mode {
    private final String identifier;
    private final List<Integer> slots;

    public Mode(String identifier) {
        this.identifier = identifier;
        this.slots = new ArrayList<>();
    }

    public Mode(String identifier, List<Integer> slots) {
        this.identifier = identifier;
        this.slots = slots;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public String getIdentifier() {
        return identifier;
    }
}
