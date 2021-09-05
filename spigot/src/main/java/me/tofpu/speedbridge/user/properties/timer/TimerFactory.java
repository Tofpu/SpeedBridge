package me.tofpu.speedbridge.user.properties.timer;

public class TimerFactory {
    public static Timer of(final int slot){
        return new TimerImpl(slot);
    }

    public static Timer of(final Integer slot, final Double result) {
        return new TimerImpl(slot, result);
    }
}
