package me.tofpu.speedbridge.user.timer;

import com.sun.istack.internal.NotNull;

public class Timer {
    private final int slot;

    private long start;
    private long end;

    private double result;

    public Timer(@NotNull final int slot) {
        this.slot = slot;
        this.start = System.currentTimeMillis();
        this.result = 0;
    }

    public Timer(@NotNull final int slot, @NotNull final double result) {
        this.slot = slot;
        this.result = result;
    }

    public Timer(@NotNull final int slot, @NotNull final long start, @NotNull final long end) {
        this(slot, end - start);
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void complete() {
        this.result = end - start;
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
