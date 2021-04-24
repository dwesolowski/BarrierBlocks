package com.github.dwesolowski.barrierblocks;

import org.bukkit.Location;
import org.bukkit.Material;
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
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) &&
                e.getClickedBlock().getType() == Material.BARRIER) {
            Location location = e.getPlayer().getLocation();
            if (GriefPreventionHook.hasClaimAtLocation(e.getPlayer(), location)) {
                if (GriefPreventionHook.isOwnerAtLocation(e.getPlayer(), location)) {
                    if (e.getItem().getType() == BarrierBlocks.settings().getBreakMaterial()) {
                        e.getClickedBlock().breakNaturally();
                        e.getClickedBlock().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), new ItemStack(Material.BARRIER));
                    }
                } else {
                    e.getPlayer().sendMessage(BarrierBlocks.language().noBreakPerms());
                    e.setCancelled(true);
                }
            } else {
                e.getClickedBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onItemPlace(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType().equals(Material.BARRIER)) {
            Location location = e.getPlayer().getLocation();
            if (!GriefPreventionHook.isOwnerAtLocation(e.getPlayer(), location)) {
                e.getPlayer().sendMessage(BarrierBlocks.language().noPlacePerms());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        BarrierBlocks.confirmations.invalidate(e.getPlayer());
    }
}
