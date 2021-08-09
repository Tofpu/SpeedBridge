package me.tofpu.speedbridge.data.file.config;

import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.data.file.config.extend.ConfigLobby;
import me.tofpu.speedbridge.data.file.config.extend.ConfigMessages;
import me.tofpu.speedbridge.data.file.config.extend.ConfigSettings;
import me.tofpu.speedbridge.data.file.config.output.impl.IntegerOutput;
import me.tofpu.speedbridge.data.file.config.output.impl.StringOutput;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.data.file.config.type.ReturnType;
import me.tofpu.speedbridge.data.file.extend.FileLobby;
import me.tofpu.speedbridge.data.file.extend.FileMessages;
import me.tofpu.speedbridge.data.file.extend.FileSettings;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final List<Config> FILE_TYPES = new ArrayList<>();
    private final String identifier;
    private FileConfiguration configuration;

    public Config(final String identifier, final FileConfiguration configuration) {
        this.identifier = identifier;
        this.configuration = configuration;
        FILE_TYPES.add(this);
    }

    public static void initialize(final SpeedBridge speedBridge) {
        new ConfigSettings(new FileSettings(speedBridge).getConfiguration()); // SLOT 0
        new ConfigMessages(new FileMessages(speedBridge).getConfiguration()); // SLOT 1
        new ConfigLobby((new FileLobby(speedBridge).getConfiguration())); // slot 2
    }

    public static Config get(final String identifier) {
        for (final Config type : FILE_TYPES) {
            if (type.getIdentifier().equalsIgnoreCase(identifier)) return type;
        }
        return null;
    }

    public static void reload(FileConfiguration... configurations) {
        for (int i = 0; i < configurations.length; i++) {
            final Config type = FILE_TYPES.get(i);
            if (type != null) type.reload(configurations[i]);
        }
    }

    public Object get(final Path path) {
        return ReturnType.of(configuration.get(path.getPath())).accept();
    }

    public void reload(final FileConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public static class TranslateOutput {
        public static String toString(Path path) {
            final String type = path.name().split("_")[0];
            return toString(Config.get(type).get(path));
        }

        public static String toString(Object o) {
            return StringOutput.of(o);
        }

        public static Integer toInteger(Path path) {
            final String type = path.name().split("_")[0];
            return toInteger(Config.get(type).get(path));
        }

        public static Integer toInteger(Object o) {
            return IntegerOutput.of(o);
        }
    }
}
