package me.tofpu.speedbridge.api.island.mode;

import me.tofpu.speedbridge.api.util.Identifier;

import java.util.List;

public interface Mode extends Identifier {
    /**
     * @return the mode initial selected slots
     */
    List<Integer> slots();

    /**
     * @return true if the settings was set to this, otherwise false
     */
    boolean isDefault();
}
