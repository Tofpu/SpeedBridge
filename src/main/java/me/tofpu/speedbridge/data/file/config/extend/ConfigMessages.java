package me.tofpu.speedbridge.data.file.config.extend;

import me.tofpu.speedbridge.data.file.FileConfig;
import me.tofpu.speedbridge.data.file.config.Config;

public class ConfigMessages extends Config {
    public ConfigMessages(final FileConfig fileConfig) {
        super("messages", fileConfig);
    }
}
