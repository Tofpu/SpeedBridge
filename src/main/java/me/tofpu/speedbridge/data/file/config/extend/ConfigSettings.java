package me.tofpu.speedbridge.data.file.config.extend;

import me.tofpu.speedbridge.data.file.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigSettings extends Config {
    public ConfigSettings(final FileConfiguration configuration) {
        super("settings", configuration);
    }
}
