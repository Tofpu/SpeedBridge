package me.tofpu.speedbridge.filetype.type;

import me.tofpu.speedbridge.filetype.type.extend.FileMessages;
import me.tofpu.speedbridge.filetype.type.extend.FileSettings;
import me.tofpu.speedbridge.filetype.type.output.impl.IntegerOutput;
import me.tofpu.speedbridge.filetype.type.output.impl.StringOutput;
import me.tofpu.speedbridge.filetype.type.path.Path;
import me.tofpu.speedbridge.filetype.type.type.ReturnType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class FileType {
    private static final List<FileType> FILE_TYPES = new ArrayList<>();

    public static void initialize(final FileConfiguration settingsConfig){
        new FileSettings(settingsConfig); // SLOT 0
        new FileMessages(); // SLOT 1
    }

    public static FileType get(final String identifier){
        for (final FileType type : FILE_TYPES){
            if (type.getIdentifier().equalsIgnoreCase(identifier)) return type;
        }
        return null;
    }

    public static void reload(FileConfiguration ...configurations){
        for (int i = 0; i < configurations.length; i++) {
            final FileType type = FILE_TYPES.get(i);
            if (type != null) type.reload(configurations[i]);
        }
    }

    private final String identifier;
    private FileConfiguration configuration;

    public FileType(final String identifier, final FileConfiguration configuration){
        this.identifier = identifier;
        this.configuration = configuration;
        FILE_TYPES.add(this);
    }

    public Object get(final Path path){
        return ReturnType.of(configuration.get(path.getPath())).accept();
    }

    public void reload(final FileConfiguration configuration){
        this.configuration = configuration;
    }

    public String getIdentifier() {
        return identifier;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public static class TranslateOutput {
        public static String toString(Path path){
            final String type = path.name().split("_")[0];
            return toString(FileType.get(type).get(path));
        }

        public static String toString(Object o) {
            return StringOutput.of(o);
        }

        public static Integer toInteger(Path path){
            final String type = path.name().split("_")[0];
            return toInteger(FileType.get(type).get(path));
        }

        public static Integer toInteger(Object o) {
            return IntegerOutput.of(o);
        }
    }
}
