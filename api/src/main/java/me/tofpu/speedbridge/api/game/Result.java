package me.tofpu.speedbridge.api.game;

public enum Result {
    /**
     * The action were successful.
     */
    SUCCESS,

    /**
     * The action failed for some reason, check the method for more information.
     */
    FAIL,

    /**
     * The island were full/not-available.
     */
    FULL,

    /**
     * There were no other available islands.
     */
    NONE,

    /**
     * There were no defined lobby location.
     */
    INVALID_LOBBY,

    /**
     * The island did not exist.
     */
    INVALID_ISLAND;
}
