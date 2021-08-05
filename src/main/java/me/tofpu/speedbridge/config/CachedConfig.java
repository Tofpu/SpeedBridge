package me.tofpu.speedbridge.config;

import org.bukkit.configuration.file.FileConfiguration;

public class CachedConfig {
    private static final String SETTINGS_PATH = "settings.";
    public static int maxSlots;
    private static FileConfiguration configuration;

    public static void initialize(final FileConfiguration configuration) {
        setConfiguration(configuration);
        CachedConfig.maxSlots = configuration.getInt(SETTINGS_PATH + "max-slots");
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(final FileConfiguration configuration) {
        CachedConfig.configuration = configuration;
    }
}
