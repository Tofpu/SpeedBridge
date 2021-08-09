package me.tofpu.speedbridge.data.file;

import me.tofpu.speedbridge.SpeedBridge;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileConfig {
    private String identifier;
    private FileConfiguration configuration;

    public FileConfig(final SpeedBridge speedBridge, final String identifier) {
        this.identifier = identifier;

        initialize(speedBridge, identifier);
    }

    public void initialize(final SpeedBridge speedBridge, final String identifier) {
        final File directory = speedBridge.getDataFolder();
        final File settingsFile = new File(directory, identifier + ".yml");

        if (!settingsFile.exists()) speedBridge.saveResource(identifier + ".yml", false);

        this.configuration = YamlConfiguration.loadConfiguration(settingsFile);
    }

    public String getIdentifier() {
        return identifier;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
