package me.tofpu.speedbridge.user.properties.timer;

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

    public void complete() {
        final long total = end - start;
        this.result = (double) total / 1000;
    }

    public int getSlot() {
        return slot;
    }

    public double getResult() {
        return result;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Timer{" +
                "slot=" + slot +
                ", result=" + result +
                '}';
    }
}
