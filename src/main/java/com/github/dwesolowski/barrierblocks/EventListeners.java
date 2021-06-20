package com.github.dwesolowski.barrierblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class EventListeners implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && b.getType() == Material.BARRIER) {
            Player p = e.getPlayer();
            Location location = b.getLocation();
            if (GriefPreventionHook.hasClaimAtLocation(p, location)) {
                if (GriefPreventionHook.isOwnerAtLocation(p, location)) {
                    if (e.getItem().getType() == BarrierBlocks.settings().getBreakMaterial()) {
                        b.breakNaturally();
                        b.getWorld().dropItemNaturally(location, new ItemStack(Material.BARRIER));
                    }
                } else {
                    e.setCancelled(true);
                    p.sendMessage(BarrierBlocks.language().noBreakPerms());
                }
            } else {
                b.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onItemPlace(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType().equals(Material.BARRIER)) {
            Player p = e.getPlayer();
            Location location = e.getBlockPlaced().getLocation();
            if (!GriefPreventionHook.hasClaimAtLocation(p, location)) {
                e.setCancelled(true);
                p.sendMessage(BarrierBlocks.language().noPlacePerms());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        BarrierBlocks.confirmations.invalidate(e.getPlayer());
    }
}
