package de.vendettagroup.real_emeralds.listener;

import de.vendettagroup.real_emeralds.Main;
import de.vendettagroup.real_emeralds.config.DataManager;
import de.vendettagroup.real_emeralds.crafting.Recipes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class Listener implements org.bukkit.event.Listener {

    private Main plugin;
    private Recipes recipes;
    private DataManager data;

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
                if ( itemPlaced.getItemMeta().hasLore()) {
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
            blockName = "m" +  Integer.toString(i) + "EmBlock";
            for (String element : itemPlaced.getItemMeta().getLore()) {
                if (ChatColor.translateAlternateColorCodes('§', element)
                        .equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(blockName + ".lore")))) {
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
        if (block.getType().equals(Material.EMERALD_BLOCK)){
            breakEmeraldBlock(block, event);
        }
        if(event.getBlock().getType() == Material.EMERALD_ORE || event.getBlock().getType() == Material.DEEPSLATE_EMERALD_ORE ) {
            breakEmeraldOre(p, event);
        }
    }

    private void breakEmeraldBlock(Block block, BlockBreakEvent event){
        switch (event.getPlayer().getInventory().getItemInMainHand().getType()){
            case WOODEN_PICKAXE:
            case STONE_PICKAXE:
            case GOLDEN_PICKAXE:
                return;
        }
        String blockLore = data.checkIfBlockPosExists(createBlockPosString(block));
        if(blockLore.equals("error")){
            return;
        }
        event.setDropItems(false);
        event.getPlayer().getWorld().dropItemNaturally(block.getLocation(),recipes.setBlockMeta(blockLore,
                new ItemStack(Material.EMERALD_BLOCK)));
        return;
    }

    private void breakEmeraldOre(Player p,BlockBreakEvent event) {
        Collection<ItemStack> blockDrop = event.getBlock().getDrops(p.getInventory().getItemInMainHand());
        ItemStack[] drops = blockDrop.toArray(new ItemStack[blockDrop.size()]);
        if (drops[0].getType().equals(Material.EMERALD)) {
            Location location = event.getBlock().getLocation();
            location.getWorld().dropItem(location, recipes.changeOutput(drops));
            event.setDropItems(false);
        }
    }

    private String blockToDrop(String lore){
        String blockName;
        for (int i =1; i <= 9; i++){
            blockName = "m" +  Integer.toString(i) + "EmBlock";
            if(lore.equals(plugin.getConfig().getString(plugin.getConfig().getString(blockName + ".lore")))) {
                return blockName;
            }
        }
        return "error";
    }
}
