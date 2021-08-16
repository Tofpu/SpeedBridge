package me.tofpu.speedbridge.data.file.config.extend;

import me.tofpu.speedbridge.data.file.FileConfig;
import me.tofpu.speedbridge.data.file.config.Config;

public class ConfigSettings extends Config {
    public ConfigSettings(final FileConfig fileConfig) {
        super("settings", fileConfig);
    }
}
