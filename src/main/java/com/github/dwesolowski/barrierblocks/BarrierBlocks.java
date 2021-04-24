package com.github.dwesolowski.barrierblocks;

import com.github.dwesolowski.barrierblocks.commands.Barrier;
import com.github.dwesolowski.barrierblocks.configuration.Language;
import com.github.dwesolowski.barrierblocks.configuration.Settings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class BarrierBlocks extends JavaPlugin implements Listener {
    private static Language language;
    private static Settings settings;

    public static final Cache<Player, ItemStack[]> confirmations = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    @Override
    public void onEnable() {
        language = new Language(this);
        settings = new Settings(this);

        reload();
        getServer().getPluginManager().registerEvents(new EventListeners(), this);
        getCommand("barrier").setExecutor(new Barrier());
    }

    public static void reload() {
        settings.reload();
        language.reload();
    }

    @Override
    public void onDisable() {
        language = null;
        settings = null;
    }

    public static Settings settings() {
        return settings;
    }

    public static Language language() {
        return language;
    }
}