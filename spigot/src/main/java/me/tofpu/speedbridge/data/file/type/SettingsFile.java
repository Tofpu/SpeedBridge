package me.tofpu.speedbridge.data.file.type;

import com.github.requestpluginsforfree.fileutil.file.PluginFile;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SettingsFile extends PluginFile {
    public SettingsFile(final Plugin plugin, final File directory) {
        super(plugin, directory, "settings.yml");
    }
}
