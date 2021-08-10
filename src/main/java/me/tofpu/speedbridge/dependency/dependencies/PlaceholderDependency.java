package me.tofpu.speedbridge.dependency.dependencies;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.tofpu.speedbridge.dependency.Dependency;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceholderDependency implements Dependency<PlaceholderAPIPlugin> {
    private PlaceholderAPIPlugin placeholderAPI;

    @Override
    public String dependencyName() {
        return "PlaceholderAPI";
    }

    @Override
    public void hook(JavaPlugin javaPlugin) {
        this.placeholderAPI = PlaceholderAPIPlugin.getInstance();
    }

    @Override
    public PlaceholderAPIPlugin getDependency() {
        return placeholderAPI;
    }
}
