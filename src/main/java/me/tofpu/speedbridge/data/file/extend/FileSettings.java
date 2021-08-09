package me.tofpu.speedbridge.data.file.extend;

import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.data.file.FileConfig;

public class FileSettings extends FileConfig {
    public FileSettings(final SpeedBridge speedBridge) {
        super(speedBridge, "settings");
    }
}
