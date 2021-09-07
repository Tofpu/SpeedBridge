package me.tofpu.speedbridge.api.game;

public enum SetupStage {

    /**
     * The island brand-new endpoint.
     */
    END_POINT,

    /**
     * The lobby location.
     */
    LOBBY,

    /**
     * The island first boarder position.
     */
    POSITION_1,

    /**
     * The island second boarder position.
     */
    POSITION_2,

    /**
     * The island spawn-point.
     */
    SPAWN;

    public static SetupStage getMatch(final String string) {
        for (final SetupStage stage : SetupStage.values()) {
            if (stage.name().replace("_", "").equalsIgnoreCase(string.toUpperCase())) return stage;
        }
        return null;
    }
}
