package de.vendettagroup.real_emeralds.crafting;

import de.vendettagroup.real_emeralds.Main;
import de.vendettagroup.real_emeralds.RealEmeralds;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Recipes implements Listener {

    private Main plugin;
    private RealEmeralds realEmeralds;

    public Recipes(Main plugin, RealEmeralds realEmeralds){
        this.plugin = plugin;
        this.realEmeralds = realEmeralds;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if(event.getRecipe() == null) {
            return;
        }
        checkForEmBlockOutput(event);
        checkForEmeraldOutput(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent e) {
        if (e.getInventory().getResult().getType().equals(Material.EMERALD)) {
            if (e.getInventory().getResult().hasItemMeta()) {
                return;
            }
            int count = 9 - e.getInventory().getResult().getAmount();
            Player p = (Player) e.getViewers().get(0);
            ItemStack[] itemStack = new ItemStack[1];
            itemStack[0] = new ItemStack(Material.EMERALD, count);
            if (e.isShiftClick()) {
                int amoutOfEmBlocks = 0;
                for (int i = 1; i <= 9; i++) {
                    if (e.getInventory().getItem(i) != null) {
                        amoutOfEmBlocks = e.getInventory().getItem(i).getAmount();
                        plugin.getLogger().info(String.valueOf(e.getInventory().getItem(i).getType()));
                        plugin.getLogger().info(String.valueOf(e.getInventory().getItem(i).getAmount()));
                        plugin.getLogger().info(String.valueOf(amoutOfEmBlocks));
                    }
                }
                plugin.getLogger().info("Counter bevor *amoutofEmBlocks: " + count);
                plugin.getLogger().info("amoutofEmBlocks: " + amoutOfEmBlocks);
                count *= amoutOfEmBlocks;
                plugin.getLogger().info("Counter nach *amoutofEmBlocks: " + count);
                plugin.getConfig().getString("Vor Loop: " + count);
                while (count > 64){
                    count -= 64;
                    plugin.getLogger().info("Während Loop: " + count);
                    itemStack[0] = new ItemStack(Material.EMERALD, 64);
                    p.getWorld().dropItemNaturally(p.getLocation(),new ItemStack(realEmeralds.changeOutput(itemStack)));
                }
                itemStack[0] = new ItemStack(Material.EMERALD, count);
                plugin.getLogger().info("Endstand: " + count);
            }
            // TODO: - Auto Drop ins Inventory;
            p.getWorld().dropItemNaturally(p.getLocation(),new ItemStack(realEmeralds.changeOutput(itemStack)));
        }
    }

    private void checkForEmeraldOutput(PrepareItemCraftEvent e) {
        if (e.getRecipe().getResult().getType().equals(Material.EMERALD)) {
            int maxDrop = 9;
            for (int i = 0; i <= 9; i++) {
                if (e.getInventory().getItem(i) != null) {
                    if (e.getInventory().getItem(i).getType().equals(Material.EMERALD_BLOCK) && e.getInventory().getItem(i).getItemMeta().hasLore()) {
                        maxDrop = maxDrop - checkForEmBlockLore(e.getInventory().getItem(i).getItemMeta().getLore());
                        if(maxDrop == 0){
                            ItemStack[] itemStack = new ItemStack[1];
                            itemStack[0] = new ItemStack(Material.EMERALD, 9);
                            e.getInventory().setResult(new ItemStack(realEmeralds.changeOutput(itemStack)));
                        } else {
                            e.getInventory().setResult(new ItemStack(Material.EMERALD, maxDrop));
                        }
                    }
                }
            }
        }
    }

    private int checkForEmBlockLore(List<String> lore) {
        String blockName;
        for (int i = 1; i <= 9; i++) {
            // To line short I create the search String here
            blockName = "m" +  Integer.toString(i) + "EmBlock";
            for (String element : lore) {
                if (ChatColor.translateAlternateColorCodes('§', element)
                        .equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(blockName + ".lore")))) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void checkForEmBlockOutput(PrepareItemCraftEvent event) {
        if (event.getRecipe().getResult().getType().equals(Material.EMERALD_BLOCK)) {
            int counter = 0;
            for (int i = 0; i <= 9; i++) {
                if (event.getInventory().getItem(i) != null) {
                    if (event.getInventory().getItem(i).getType().equals(Material.EMERALD) && event.getInventory().getItem(i).getItemMeta().hasLore()){
                        if (checkForLore(event.getInventory().getItem(i).getItemMeta().getLore())) {
                            counter++;
                        }
                    }
                }
            }
            if(counter == 0) {
                return;
            }
            if (counter != 0) {
                String blockName = "m" +  Integer.toString(counter) + "EmBlock";

                event.getInventory().setResult(setBlockMeta(blockName, event.getRecipe().getResult()));
            }
        }
    }

    public ItemStack setBlockMeta(String blockName, ItemStack item){
        ItemMeta meta = item.getItemMeta();
        // Data from Config gets here added
        // DisplayName
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                plugin.getConfig().getString(blockName + ".displayName")));
        // CustomeModelData
        if (plugin.getConfig().getInt(blockName + ".customModelData") != 0) {
            meta.setCustomModelData(plugin.getConfig().getInt("mEmerald.customModelData"));
        }
        // Lore
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(blockName + ".lore")));
        meta.setLore(lore);
        // Glow effect
        if (plugin.getConfig().getBoolean(blockName + ".glowEffect")) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        ItemStack result = new ItemStack(Material.EMERALD_BLOCK,1);
        result.setItemMeta(meta);
        return result;
    }


    // I dont know shit
    // Mc converted & from the config to § ! WHY??????????? This toke so long to find out :(
    // This could be probably be optimized but as i like to say i will look into this tomorrow :)
    private boolean checkForLore(List<String> list){
        for (String element: list) {
            if (ChatColor.translateAlternateColorCodes('§',element)
                    .equals(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("mEmerald.lore")))) {
                return true;
            }
        }
        return false;
    }
}