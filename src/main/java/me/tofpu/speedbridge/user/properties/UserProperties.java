package me.tofpu.speedbridge.user.properties;

import me.tofpu.speedbridge.user.properties.timer.Timer;

public interface UserProperties {
    /**
     * @return the player's island slot they're in, otherwise null
     */
    Integer islandSlot();

    /**
     * @param slot setting the player island slot
     * @apiNote Do not use this, unless you know what you're doing
     */
    void islandSlot(final Integer slot);

    /**
     * @return the player's personal best timer, otherwise null
     */
    Timer timer();


    /**
     * @param timer the new personal best timer
     * @apiNote Do not use this, unless you know what you're doing
     */
    void timer(final Timer timer);
}
