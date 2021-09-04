package me.tofpu.speedbridge.dependency;

import org.bukkit.plugin.java.JavaPlugin;

public interface Dependency<T> {
    /**
     * Gets the dependency name, this will
     * usually be named after the plugin's name.
     *
     * @return the dependency name
     */
    String dependencyName();

    /**
     * Hooks into the dependency, usually for
     * getting a reference of the dependency.
     *
     * @param javaPlugin acm's instance
     */
    void hook(JavaPlugin javaPlugin);

    /**
     * Gets the dependency class. This will return
     * null if the plugin isn't provided.
     *
     * @return the dependency class
     */
    T getDependency();
}
