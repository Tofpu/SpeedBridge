package me.tofpu.speedbridge.game.controller.stage;

public enum SetupStage {
    SPAWN, POINT, SELECTION_A, SELECTION_B;

    public static SetupStage getMatch(final String string){
        for (final SetupStage stage : SetupStage.values()){
            if (stage.name().equalsIgnoreCase(string.toUpperCase().replace("-", "_"))) return stage;
        }
        return null;
    }
}
