package com.github.dwesolowski.barrierblocks;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GriefPreventionHook extends JavaPlugin {

    public static Claim getClaimAtLocation(final Player p, final Location loc) {
        final PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(p.getUniqueId());
        return GriefPrevention.instance.dataStore.getClaimAt(loc, true, playerData.lastClaim);
    }

    public static boolean hasClaimAtLocation(final Player p, final Location loc) {
        return getClaimAtLocation(p, loc) != null;
    }

    public static boolean isOwnerAtLocation(final Player p, final Location loc) {
        return hasClaimAtLocation(p, loc) && getClaimAtLocation(p, loc).getOwnerName().equalsIgnoreCase(p.getName());
    }
}
