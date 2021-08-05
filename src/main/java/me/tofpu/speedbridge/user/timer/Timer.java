package me.tofpu.speedbridge.user.timer;

import com.sun.istack.internal.NotNull;

public class Timer {
    private final int slot;
    private final double result;

    public Timer(@NotNull final int slot, @NotNull final double result){
        this.slot = slot;
        this.result = result;

    }

    public Timer(@NotNull final int slot, @NotNull final long start, @NotNull final long end) {
        this(slot, end - start);
    }

    @NotNull
    public int getSlot() {
        return slot;
    }

    @NotNull
    public double getResult() {
        return result;
    }
}
