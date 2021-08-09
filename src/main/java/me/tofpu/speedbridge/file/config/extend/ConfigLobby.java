package me.tofpu.speedbridge.file.config.extend;

import me.tofpu.speedbridge.file.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLobby extends Config {
    public ConfigLobby(FileConfiguration configuration) {
        super("lobby", configuration);
    }
}
