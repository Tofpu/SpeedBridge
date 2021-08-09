package me.tofpu.speedbridge.data.file.config.extend;

import me.tofpu.speedbridge.data.file.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLobby extends Config {
    public ConfigLobby(FileConfiguration configuration) {
        super("lobby", configuration);
    }
}
