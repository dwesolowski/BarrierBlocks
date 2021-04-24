package com.github.dwesolowski.barrierblocks.commands;

import com.github.dwesolowski.barrierblocks.BarrierBlocks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Barrier implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(BarrierBlocks.language().invalidSender());
            return true;
        }
        if (!cs.hasPermission("barrierblocks.barrier")) {
            cs.sendMessage(BarrierBlocks.language().noPermission());
            return true;
        }
        Player p = (Player) cs;
        if (args.length == 0) {
            BarrierBlocks.language().getCommandHelp().forEach(msg -> cs.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("{AMOUNT}", String.valueOf(BarrierBlocks.settings().getPurchaseQuantity()))
                    .replace("{COST}", String.valueOf(BarrierBlocks.settings().getCostAmount()))
                    .replace("{MATERIAL}", String.valueOf(BarrierBlocks.settings().getCostMaterial()).toLowerCase())
                    .replace("{ITEM}", String.valueOf(BarrierBlocks.settings().getBreakMaterial()).toLowerCase())));
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("barrierblocks.admin")) {
                    BarrierBlocks.reload();
                    p.sendMessage(BarrierBlocks.language().configReloaded());
                } else
                    p.sendMessage(BarrierBlocks.language().noPermission());
                return true;
            } else if (args[0].equalsIgnoreCase("accept")) {
                ItemStack[] items = BarrierBlocks.confirmations.getIfPresent(p);
                if (items == null) {
                    p.sendMessage(BarrierBlocks.language().noPending());
                    return true;
                }
                ItemStack cost = items[0];
                ItemStack barrier = items[1];
                if (fullInventory(p, barrier.getAmount())) {
                    p.sendMessage(BarrierBlocks.language().fullInventory());
                    return true;
                }
                if (!p.getInventory().containsAtLeast(cost, cost.getAmount())) {
                    p.sendMessage(BarrierBlocks.language().invalidBalance(BarrierBlocks.settings().getCostMaterial(), barrier.getAmount()));
                    return true;
                }
                p.getInventory().removeItem(cost);
                p.getInventory().addItem(barrier);
                p.sendMessage(BarrierBlocks.language().confirmAccept());
                BarrierBlocks.confirmations.invalidate(p);
                return true;
            } else if (args[0].equalsIgnoreCase("deny")) {
                p.sendMessage(BarrierBlocks.confirmations.getIfPresent(p) == null ? BarrierBlocks.language().noPending() : BarrierBlocks.language().confirmDeny());
                BarrierBlocks.confirmations.invalidate(p);
                return true;
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("buy")) {
            int amountBarrier;
            try {
                amountBarrier = Integer.parseInt(args[1]) * BarrierBlocks.settings().getPurchaseQuantity();
            } catch (NumberFormatException e) {
                cs.sendMessage(BarrierBlocks.language().invalidArgument(args[1]));
                return true;
            }
            if (amountBarrier < 1) {
                cs.sendMessage(BarrierBlocks.language().invalidNumber());
                return true;
            }
            if (fullInventory(p, amountBarrier)) {
                p.sendMessage(BarrierBlocks.language().fullInventory());
                return true;
            }
            int cost = amountBarrier / BarrierBlocks.settings().getPurchaseQuantity() * BarrierBlocks.settings().getCostAmount();
            ItemStack item = new ItemStack(BarrierBlocks.settings().getCostMaterial(), cost);
            if (!p.getInventory().containsAtLeast(item, cost)) {
                p.sendMessage(BarrierBlocks.language().invalidBalance(BarrierBlocks.settings().getCostMaterial(), amountBarrier));
                return true;
            }
            ItemStack[] items = {item, new ItemStack(Material.BARRIER, amountBarrier)};
            BarrierBlocks.confirmations.put(p, items);
            BarrierBlocks.language().getConfirmation().forEach(msg -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
                    .replace("{COST}", String.valueOf(cost))
                    .replace("{MATERIAL}", String.valueOf(BarrierBlocks.settings().getCostMaterial()).toLowerCase())
                    .replace("{AMOUNT}", String.valueOf(amountBarrier))));
            return true;
        }
        return false;
    }

    private boolean fullInventory(Player p, int amount){
        int index = p.getInventory().firstEmpty();
        if (index == -1)
            index = p.getInventory().first(Material.BARRIER);
        return index == -1 || (p.getInventory().getItem(index) != null && p.getInventory().getItem(index).getAmount() + amount > 64);
    }
}