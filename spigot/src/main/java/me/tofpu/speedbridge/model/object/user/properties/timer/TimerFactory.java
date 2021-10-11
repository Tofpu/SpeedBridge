package me.tofpu.speedbridge.model.object.user.properties.timer;

import me.tofpu.speedbridge.api.user.timer.Timer;

public class TimerFactory {
    public static Timer of(final int slot){
        return new TimerImpl(slot);
    }

    public static Timer of(final Integer slot, final Double result) {
        return new TimerImpl(slot, result);
    }
}
