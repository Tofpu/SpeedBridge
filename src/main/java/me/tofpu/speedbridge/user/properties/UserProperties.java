package me.tofpu.speedbridge.user.properties;

import me.tofpu.speedbridge.user.timer.Timer;

public class UserProperties {
    private Integer islandSlot;
    private Timer timer;

    public UserProperties() {
    }

    public UserProperties(final int islandSlot) {
        this.islandSlot = islandSlot;
    }


    public Integer getIslandSlot() {
        return islandSlot;
    }

    public void setIslandSlot(final Integer islandSlot) {
        this.islandSlot = islandSlot;
    }


    public Timer getTimer() {
        return timer;
    }

    public void setTimer(final Timer timer) {
        this.timer = timer;
    }
}
