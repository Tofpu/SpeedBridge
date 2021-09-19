package me.tofpu.speedbridge.game.processor;

import me.tofpu.speedbridge.game.processor.game.SpectatorProcessor;
import me.tofpu.speedbridge.game.processor.item.SpectatorItemProcessor;
import me.tofpu.speedbridge.game.processor.type.GameItemProcessor;
import me.tofpu.speedbridge.game.processor.type.GameProcessor;

public class Processor {
    public static final GameProcessor GAME_SPECTATOR = new SpectatorProcessor();

    public static final GameItemProcessor ITEM_SPECTATOR =
            new SpectatorItemProcessor();
}
