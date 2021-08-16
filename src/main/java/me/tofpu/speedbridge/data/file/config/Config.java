package me.tofpu.speedbridge.data.file.config;

import me.tofpu.speedbridge.SpeedBridge;
import me.tofpu.speedbridge.data.file.FileConfig;
import me.tofpu.speedbridge.data.file.config.extend.ConfigMessages;
import me.tofpu.speedbridge.data.file.config.extend.ConfigSettings;
import me.tofpu.speedbridge.data.file.config.output.impl.IntegerOutput;
import me.tofpu.speedbridge.data.file.config.output.impl.StringListOutput;
import me.tofpu.speedbridge.data.file.config.output.impl.StringOutput;
import me.tofpu.speedbridge.data.file.config.output.type.OutputType;
import me.tofpu.speedbridge.data.file.config.path.Path;
import me.tofpu.speedbridge.data.file.config.type.ReturnType;
import me.tofpu.speedbridge.data.file.extend.FileMessages;
import me.tofpu.speedbridge.data.file.extend.FileSettings;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final List<Config> FILE_TYPES = new ArrayList<>();
    private final String identifier;
    private final FileConfig fileConfig;

    private FileConfiguration configuration;

    public Config(final String identifier, final FileConfig fileConfig) {
        this.identifier = identifier;
        this.fileConfig = fileConfig;
        this.configuration = fileConfig.getConfiguration();
        FILE_TYPES.add(this);
    }

    public static void initialize(final SpeedBridge plugin) {
        new ConfigSettings(new FileSettings(plugin)); // SLOT 0
        new ConfigMessages(new FileMessages(plugin)); // SLOT 1
    }

    public static Config get(final String identifier) {
        for (final Config type : FILE_TYPES) {
            if (type.getIdentifier().equalsIgnoreCase(identifier)) return type;
        }
        return null;
    }

    public static void reload(final SpeedBridge plugin) {
        for (final Config type : FILE_TYPES) {
            type.fileConfig.initialize(plugin, type.getIdentifier());
            type.configuration = type.fileConfig.getConfiguration();
        }
    }

    public Object get(final Path path) {
        return ReturnType.of(configuration.get(path.getPath())).accept();
    }

    public Object get(final Path path, final OutputType type) {
        switch (type) {
            case STRING_LIST:
                return configuration.getStringList(path.getPath());
            case STRING:
                return configuration.getString(path.getPath());
            case INTEGER:
                return configuration.getInt(path.getPath());
            default:
                return new IllegalStateException("Unexpected value: " + type);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public static class TranslateOutput {
        public static String to(Path path) {
            switch (path.getType()) {
                case INTEGER:
                    return toInteger(path) + "";
                case STRING:
                    return toString(path);
                case STRING_LIST:
                    return toList(path);
                default:
                    throw new IllegalStateException("Unexpected value: " + path.getType());
            }
        }

        public static String toString(Path path) {
            final String type = path.name().split("_")[0];
            return toString(Config.get(type).get(path));
        }

        private static String toString(Object o) {
            return StringOutput.of(o);
        }

        public static Integer toInteger(Path path) {
            final String type = path.name().split("_")[0];
            return toInteger(Config.get(type).get(path));
        }

        public static String toList(Path path) {
            final String type = path.name().split("_")[0];

            final List<String> list = StringListOutput.of(Config.get(type).get(path));
            final StringBuilder builder = new StringBuilder();

            for (final String string : list) {
                if (builder.length() != 1) builder.append("\n");
                builder.append(string);
            }
            return builder.toString();
        }

        private static Integer toInteger(Object o) {
            return IntegerOutput.of(o);
        }
    }
}
