package me.tofpu.speedbridge.model.object.user.properties.timer;

import me.tofpu.speedbridge.api.user.timer.Timer;
import me.tofpu.speedbridge.util.Util;

import java.util.Objects;

public class TimerImpl implements Timer {
    private final int slot;

    private long start = 0;
    private long end = 0;

    private double result;

    public TimerImpl(final int slot) {
        this.slot = slot;
        this.start = System.currentTimeMillis();
        this.result = 0;
    }

    public TimerImpl(final int slot, final double result) {
        this.slot = slot;
        this.result = result;
    }

    @Override
    public int slot() {
        return this.slot;
    }

    @Override
    public double result() {
        return this.result;
    }

    @Override
    public long start() {
        return this.start;
    }

    @Override
    public void end(long end) {
        this.end = end;
        this.result = Util.toSeconds(start, end);
    }

    @Override
    public long end() {
        return this.end;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Timer{");
        sb.append("slot=").append(slot);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof TimerImpl)) return false;
        final TimerImpl timer = (TimerImpl) o;
        return slot == timer.slot && start == timer.start && end == timer.end && Double.compare(timer.result, result) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(slot(), start(), end(), result());
    }
}
