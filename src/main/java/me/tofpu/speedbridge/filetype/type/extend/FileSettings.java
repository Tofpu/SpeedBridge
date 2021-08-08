package me.tofpu.speedbridge.filetype.type.extend;

import me.tofpu.speedbridge.filetype.type.FileType;
import org.bukkit.configuration.file.FileConfiguration;

public class FileSettings extends FileType {
    public FileSettings(final FileConfiguration configuration) {
        super("settings", configuration);
    }
}
