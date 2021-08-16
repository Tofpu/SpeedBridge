package me.tofpu.speedbridge.data.file.config.extend;

import me.tofpu.speedbridge.data.file.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigMessages extends Config {
    public ConfigMessages(final FileConfiguration configuration) {
        super("messages", configuration);
    }
}
