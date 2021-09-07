package me.tofpu.speedbridge.api.user;

import me.tofpu.speedbridge.api.user.timer.Timer;

/**
 * The user property. It includes information regarding the island they're in, timer, etc.
 */
public interface UserProperties {
    /**
     * @return the player's island slot they're in, otherwise null
     */
    Integer islandSlot();

    /**
     * @param slot setting the player island slot
     */
    void islandSlot(final Integer slot);

    /**
     * @return the player's personal best timer, otherwise null
     */
    Timer timer();

    /**
     * @param timer the new personal best timer
     */
    void timer(final Timer timer);
}
