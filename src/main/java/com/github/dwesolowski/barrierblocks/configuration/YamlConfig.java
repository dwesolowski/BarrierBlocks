package com.github.dwesolowski.barrierblocks.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public abstract class YamlConfig {
    private final File file;
    final Plugin instance;

    YamlConfig(Plugin plugin, String fileName) {
        instance = plugin;
        file = new File(instance.getDataFolder(), fileName + ".yml");
    }

    private void copyDefault() {
        instance.saveResource(file.getName(), false);
    }

    public void reload() {
        if (!file.exists())
            copyDefault();
        reload(YamlConfiguration.loadConfiguration(file));
    }

    protected abstract void reload(YamlConfiguration config);
}