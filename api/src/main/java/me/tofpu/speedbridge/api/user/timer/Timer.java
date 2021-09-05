package me.tofpu.speedbridge.api.user.timer;

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
     */
    long end();
}
