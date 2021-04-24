package com.github.dwesolowski.barrierblocks.configuration;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings extends YamlConfig {

    private Material costMaterial, breakMaterial;
    private int costAmount, purchaseQuantity;

    public Settings(Plugin plugin) {
        super(plugin, "Settings");
    }

    public Material getCostMaterial() {
        return costMaterial;
    }

    public int getCostAmount() {
        return costAmount;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public Material getBreakMaterial() {
        return breakMaterial;
    }

    @Override
    protected void reload(YamlConfiguration settings) {
        costMaterial = Material.getMaterial(settings.getString("cost.type", "DIAMOND"));
        costAmount = settings.getInt("cost.amount", 1);
        purchaseQuantity = settings.getInt("purchaseQuantity", 1);
        breakMaterial = Material.getMaterial(settings.getString("breakMaterial", "GOLDEN_AXE"));
    }
}