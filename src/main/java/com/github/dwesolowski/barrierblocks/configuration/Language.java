package com.github.dwesolowski.barrierblocks.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Language extends YamlConfig {

    private String INVALID_SENDER, NO_PERMISSIONS, CONFIG_RELOADED, NO_PENDING, FULL_INVENTORY, INVALID_BALANCE, CONFIRM_ACCEPT,
            CONFIRM_DENY, INVALID_ARGUMENT, INVALID_NUMBER, NO_BREAK_PERMS, NO_PLACE_PERMS, WARNING_CREATIVE;

    public List<String> COMMAND_HELP, CONFIRMATION;

    public Language(Plugin plugin) {
        super(plugin, "Language");
    }

    @Override
    protected void reload(YamlConfiguration config) {

        INVALID_SENDER = getValue(config, "InvalidSender");

        NO_PERMISSIONS = getValue(config, "NoPermission");

        CONFIG_RELOADED = getValue(config, "ConfigReloaded");

        NO_PENDING = getValue(config, "NoPending");

        FULL_INVENTORY = getValue(config, "FullInventory");

        INVALID_BALANCE = getValue(config, "InvalidBalance");

        CONFIRM_ACCEPT = getValue(config, "ConfirmAccept");

        CONFIRM_DENY = getValue(config, "ConfirmDeny");

        INVALID_ARGUMENT = getValue(config, "InvalidArgument");

        INVALID_NUMBER = getValue(config, "InvalidNumber");

        NO_BREAK_PERMS = getValue(config, "NoBreakPerms");

        NO_PLACE_PERMS = getValue(config, "NoPlacePerms");

        COMMAND_HELP = config.getStringList("CommandHelp");

        CONFIRMATION = config.getStringList("Confirmation");

        WARNING_CREATIVE = getValue(config, "WarningCreative");
    }

    public String invalidSender() {
        return INVALID_SENDER;
    }

    public String noPermission() {
        return NO_PERMISSIONS;
    }

    public String configReloaded() {
        return CONFIG_RELOADED;
    }

    public String noPending() {
        return NO_PENDING;
    }

    public String fullInventory() {
        return FULL_INVENTORY;
    }

    public String invalidBalance(Material item, int amt) {
        return INVALID_BALANCE.replace("{MATERIAL}", String.valueOf(item).toLowerCase()).replace("{AMOUNT}", String.valueOf(amt));
    }

    public String confirmAccept() {
        return CONFIRM_ACCEPT;
    }

    public String confirmDeny() {
        return CONFIRM_DENY;
    }

    public String invalidArgument(String args) {
        return INVALID_ARGUMENT.replace("{ARGS}", args);
    }

    public String invalidNumber() {
        return INVALID_NUMBER;
    }

    public String noBreakPerms() {
        return NO_BREAK_PERMS;
    }

    public String noPlacePerms() {
        return NO_PLACE_PERMS;
    }

    public String warningCreative() {
        return WARNING_CREATIVE;
    }

    public List<String> getCommandHelp() {
        return COMMAND_HELP;
    }

    public List<String> getConfirmation() {
        return CONFIRMATION;
    }

    private String getValue(YamlConfiguration config, String lookUp) {
        String input = config.getString(lookUp);
        if (input == null) {
            Bukkit.getConsoleSender()
                    .sendMessage("§8[§bBarrierBlocks§8] §c>>> \"§f" + lookUp + "§c\" is not a valid Language Key!");
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', input).trim();
    }
}
