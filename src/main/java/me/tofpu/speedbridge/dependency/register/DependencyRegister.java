package me.tofpu.speedbridge.dependency.register;

import me.tofpu.speedbridge.dependency.Dependency;
import me.tofpu.speedbridge.dependency.dependencies.PlaceholderDependency;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DependencyRegister {
    private final static List<Dependency<?>> DEPENDENCIES = new ArrayList<>();

    static {
        DEPENDENCIES.add(new PlaceholderDependency());
    }

    public static void loadAll(JavaPlugin javaPlugin) {
        for (Dependency<?> dependency : DEPENDENCIES) {
            if (!Bukkit.getPluginManager().isPluginEnabled(dependency.dependencyName())) {
                continue;
            }
            dependency.hook(javaPlugin);
        }
    }

    public static Dependency<?> get(String name) {
        for (Dependency<?> dependency : DEPENDENCIES) {
            if (dependency.dependencyName().equalsIgnoreCase(name)) {
                return dependency;
            }
        }
        return null;
    }

    public static List<Dependency<?>> getDependencies() {
        return DEPENDENCIES;
    }
}
