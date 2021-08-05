package me.tofpu.speedbridge.config;

import com.sun.istack.internal.NotNull;
import org.bukkit.configuration.file.FileConfiguration;

public class CachedConfig {
    private static final String SETTINGS_PATH = "settings.";

    private static FileConfiguration configuration;
    public static int maxSlots;

    public static void initialize(@NotNull final FileConfiguration configuration){
        setConfiguration(configuration);
        CachedConfig.maxSlots = configuration.getInt(SETTINGS_PATH + "max-slots");
    }

    public static void setConfiguration(@NotNull final FileConfiguration configuration) {
        CachedConfig.configuration = configuration;
    }

    public static FileConfiguration getConfiguration() {
        return configuration;
    }
}
