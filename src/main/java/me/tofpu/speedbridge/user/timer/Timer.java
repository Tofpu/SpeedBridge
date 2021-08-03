package me.tofpu.speedbridge.user.timer;

import com.sun.istack.internal.NotNull;

public class Timer {
    private final long start;
    private final long end;

    public Timer(@NotNull final long start, @NotNull final long end) {
        this.start = start;
        this.end = end;
    }

    @NotNull
    public long getStart() {
        return start;
    }

    @NotNull
    public long getEnd() {
        return end;
    }

    @NotNull
    public long getResult(){
        return end - start;
    }
}
