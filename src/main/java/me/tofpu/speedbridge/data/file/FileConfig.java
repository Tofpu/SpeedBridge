package me.tofpu.speedbridge.data.file;

import me.tofpu.speedbridge.SpeedBridge;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileConfig {
    private final String identifier;
    private FileConfiguration configuration;

    public FileConfig(final SpeedBridge plugin, final String identifier) {
        this.identifier = identifier;

        initialize(plugin, identifier);
    }

    public void initialize(final SpeedBridge plugin, final String identifier) {
        final File directory = plugin.getDataFolder();
        final File settingsFile = new File(directory, identifier + ".yml");

        if (!settingsFile.exists()) plugin.saveResource(identifier + ".yml", false);

        this.configuration = YamlConfiguration.loadConfiguration(settingsFile);
    }

    public String getIdentifier() {
        return identifier;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
