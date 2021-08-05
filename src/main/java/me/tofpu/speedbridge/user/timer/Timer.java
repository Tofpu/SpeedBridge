package me.tofpu.speedbridge.user.timer;

public class Timer {
    private final int slot;

    private long start;
    private long end;

    private double result;

    public Timer(final int slot) {
        this.slot = slot;
        this.start = System.currentTimeMillis();
        this.result = 0;
    }

    public Timer(final int slot, final double result) {
        this.slot = slot;
        this.result = result;
    }

    public Timer(final int slot, final long start, final long end) {
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


    public int getSlot() {
        return slot;
    }


    public double getResult() {
        return result;
    }
}
