package me.tofpu.speedbridge.user.properties.timer;

public interface Timer {
    /**
     * @return the island slot
     */
    int slot();

    /**
     * @return the total result if the end timestamp has been provided, otherwise 0
     */
    double result();

    /**
     * The start timestamp when it has started
     *
     * @return the start timestamp
     * @apiNote this isn't stored thus, will return 0 upon restart
     */
    long start();

    /**
     * This will set the end timestamp and provide the result
     *
     * @param end the end timestamp
     */
    void end(final long end);

    /**
     * @return the end timestamp
     * @apiNote this isn't stored thus, will return 0 upon restart
     */
    long end();
}
