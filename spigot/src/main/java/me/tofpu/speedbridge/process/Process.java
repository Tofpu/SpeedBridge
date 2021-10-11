package me.tofpu.speedbridge.process;

import me.tofpu.speedbridge.process.item.JoinItemProcessor;
import me.tofpu.speedbridge.process.processors.JoinProcessor;
import me.tofpu.speedbridge.process.processors.SpectatorProcessor;
import me.tofpu.speedbridge.process.item.SpectatorItemProcessor;
import me.tofpu.speedbridge.process.processors.TimerUpdateProcessor;
import me.tofpu.speedbridge.process.type.GameItemProcessor;
import me.tofpu.speedbridge.process.type.GameProcessor;

public class Process {
    public static final GameProcessor GAME_JOIN = new JoinProcessor();
    public static final GameProcessor GAME_SPECTATOR = new SpectatorProcessor();
    public static final GameProcessor GAME_UPDATE = new TimerUpdateProcessor();

    public static final GameItemProcessor ITEM_JOIN = new JoinItemProcessor();
    public static final GameItemProcessor ITEM_SPECTATOR =
            new SpectatorItemProcessor();
}
