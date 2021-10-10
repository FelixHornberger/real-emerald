package de.vendettagroup.real_emeralds.listener;

import de.vendettagroup.real_emeralds.Main;
import de.vendettagroup.real_emeralds.config.DataManager;
import de.vendettagroup.real_emeralds.crafting.Recipes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Listener implements org.bukkit.event.Listener {

    private  Main plugin;
    private  Recipes recipes;
    private  DataManager data;

    public Listener(Main plugin, DataManager data, Recipes recipes) {
        this.plugin = plugin;
        this.data = data;
        this.recipes = recipes;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        if(event.getRecipe() == null) {
            return;
        }
        recipes.checkForEmBlockOutput(event);
        recipes.checkForEmeraldOutput(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCraftItem(CraftItemEvent e) {
        recipes.craftEmerald(e);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType().equals(Material.EMERALD_BLOCK)){
            ItemStack itemPlaced;
            if (player.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK)){
                itemPlaced = player.getInventory().getItemInMainHand();
            } else {
                itemPlaced = player.getInventory().getItemInOffHand();
            }
            if (itemPlaced.hasItemMeta()){
                if ( Objects.requireNonNull(itemPlaced.getItemMeta()).hasLore()) {
                    String blockName = checkForLore(itemPlaced);
                    if (blockName.equals("Not found")) {
                        return;
                    }
                    data.createBlockPosFile(createBlockPosString(block),blockName);
                }
            }
        }
    }

    private String createBlockPosString(Block block) {
        return block.getLocation().getBlockX() + "_" + block.getLocation().getBlockY()+ "_" +block.getLocation().getBlockZ();
    }

    private String checkForLore(ItemStack itemPlaced){
        String blockName;
        for (int i = 1; i <= 9; i++) {
            blockName = "m" + i + "EmBlock";
            for (String element : Objects.requireNonNull(Objects.requireNonNull(itemPlaced.getItemMeta()).getLore())) {
                if (ChatColor.translateAlternateColorCodes('§', element)
                        .equals(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString(blockName + ".lore"))))) {
                    return blockName;
                }
            }
        }
        return "Not found";
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player p = event.getPlayer();
        Collection<ItemStack> collection = block.getDrops(p.getInventory().getItemInMainHand());
        ItemStack[] drops = collection.toArray(new ItemStack[collection.size()]);
        if (drops.length == 0){
            return;
        }
        if (drops[0].getType().equals(Material.EMERALD_BLOCK)){
            breakEmeraldBlock(block, event);
        }
        if (drops[0].getType().equals(Material.EMERALD)){
            breakEmeraldOre(p, event);
        }
    }

    private void breakEmeraldBlock(Block block, BlockBreakEvent event){
        String blockLore = data.checkIfBlockPosExists(createBlockPosString(block));
        if(blockLore.equals("error")){
            return;
        }
        event.setDropItems(false);
        event.getPlayer().getWorld().dropItemNaturally(block.getLocation(),recipes.setBlockMeta(blockLore,
                new ItemStack(Material.EMERALD_BLOCK)));
    }

    private void breakEmeraldOre(Player p,BlockBreakEvent event) {
        Collection<ItemStack> blockDrop = event.getBlock().getDrops(p.getInventory().getItemInMainHand());
        ItemStack[] drops = blockDrop.toArray(new ItemStack[0]);
        if (drops[0].getType().equals(Material.EMERALD)) {
            Location location = event.getBlock().getLocation();
            Objects.requireNonNull(location.getWorld()).dropItem(location, recipes.changeOutput(drops));
            event.setDropItems(false);
        }
    }

}
