package me.tofpu.speedbridge.island.mode.manager;

import me.tofpu.speedbridge.file.config.Config;
import me.tofpu.speedbridge.island.mode.Mode;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ModeManager {
    private final static ModeManager MODE_MANAGER = new ModeManager();
    private final List<Mode> modes;

    public ModeManager() {
        this.modes = new ArrayList<>();
    }

    public static ModeManager getModeManager() {
        return MODE_MANAGER;
    }

    public void initialize() {
        final ConfigurationSection section = Config.get("settings").getConfiguration().getConfigurationSection("modes");

        for (final String value : section.getKeys(false)) {
            final Mode mode = new Mode(value);
            final String[] args = section.getString(value).split("-");

            final int integer = Integer.parseInt(args[0]);

            final List<Integer> list = new ArrayList<>();
            boolean hasArray = false;
            if (args.length > 1) {
                hasArray = true;
                for (int i = integer; i <= Integer.parseInt(args[1]); i++) {
                    list.add(i);
                }
            }
            if (hasArray) mode.getSlots().addAll(list);
            else mode.getSlots().add(integer);

            getModes().add(mode);
        }
    }

    public Mode get(final int slot) {
        for (final Mode mode : modes) {
            if (mode.getSlots().contains(slot)) return mode;
        }
        return null;
    }

    public Mode get(final String identifier) {
        for (final Mode mode : modes) {
            if (mode.getIdentifier().equals(identifier)) return mode;
        }
        return null;
    }

    public List<Mode> getModes() {
        return modes;
    }
}
