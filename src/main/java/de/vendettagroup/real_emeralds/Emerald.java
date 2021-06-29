package de.vendettagroup.real_emeralds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class Emerald implements Listener {

    // Trough the tags i make sure this event only "fires" when the block is realy brocken
    // First i check the Block is an emerald ore, i saw 1.17 added a new emerald ore,
    // but unsure if you can get it in surival but added it for sure
    // To change the drop i get the location of the block and change the ouput trough the dropItem Method
    // event.setDropItems(false) is used to make sure this event doesnt drop the normal item. :)

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.EMERALD_ORE || event.getBlock().getType() == Material.DEEPSLATE_EMERALD_ORE ) {
            Player p = event.getPlayer();
            Collection <ItemStack> blockDrop = event.getBlock().getDrops(p.getInventory().getItemInMainHand());
            ItemStack[] drops = blockDrop.toArray(new ItemStack[blockDrop.size()]);
            if (drops[0].getType().equals(Material.EMERALD)) {
                Location location = event.getBlock().getLocation();
                location.getWorld().dropItem(location, changeOutput(drops));
                event.setDropItems(false);
            }
        }
    }

    // Adds an lore to the emerald, so there is an difference between traded and mined emeralds
    private ItemStack changeOutput(ItemStack[] drops) {
        ItemMeta meta = drops[0].getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_RED + "Dropped out of an Stone");
        meta.setLore(lore);
        drops[0].setItemMeta(meta);
        return drops[0];
    }

}
