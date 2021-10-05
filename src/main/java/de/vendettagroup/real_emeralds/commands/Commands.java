package de.vendettagroup.real_emeralds.commands;

import de.vendettagroup.real_emeralds.config.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Commands implements CommandExecutor {

    private final Plugin plugin;
    private final DataManager dataManager;

    public Commands(Plugin plugin, DataManager dataManager){
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("realemerald")) {
            if (!sender.hasPermission("realemerald.admin")) {
                sender.sendMessage(ChatColor.RED + "You dont have permission to this command");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /realemerald reload");
                sender.sendMessage(ChatColor.RED + "Usage: /realemerald clearloreLog");
                return true;
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")){
                    sender.sendMessage("Realemerald reloaded! ");
                    plugin.reloadConfig();
                    dataManager.checkIfLoreIsAlreadyInLog();
                    return true;
                }
                if (args[0].equalsIgnoreCase("clear-lore-log")){
                    sender.sendMessage("Realemerald loreLog cleared!");
                    dataManager.clearLoreLog();
                    return true;
                }
            }
        }
        return false;
    }
}
