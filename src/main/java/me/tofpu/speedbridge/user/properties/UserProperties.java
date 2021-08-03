package me.tofpu.speedbridge.user.properties;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import me.tofpu.speedbridge.user.timer.Timer;

public class UserProperties {
    private Integer islandSlot;
    private Timer timer;

    public UserProperties(){}

    public UserProperties(@NotNull final int islandSlot) {
        this.islandSlot = islandSlot;
    }

    public void setTimer(@NotNull final Timer timer) {
        this.timer = timer;
    }

    @Nullable
    public Integer getIslandSlot() {
        return islandSlot;
    }

    @Nullable
    public Timer getTimer() {
        return timer;
    }
}
