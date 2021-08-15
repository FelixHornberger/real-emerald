package de.vendettagroup.real_emeralds.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Commands implements CommandExecutor {

    private Plugin plugin;

    public Commands(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("realemerald")) {
            if (!sender.hasPermission("realemerald.reload")) {
                sender.sendMessage(ChatColor.RED + "You dont have permission to this command");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /realemerald reload");
                return true;
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")){
                    sender.sendMessage("Reloaded ");
                    plugin.reloadConfig();
                    return true;
                }
            }
        }
        return false;
    }
}
