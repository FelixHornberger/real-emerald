package de.vendettagroup.real_emeralds;

import de.vendettagroup.real_emeralds.commands.Commands;
import de.vendettagroup.real_emeralds.config.DataManager;
import de.vendettagroup.real_emeralds.crafting.Recipes;
import de.vendettagroup.real_emeralds.listener.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private RealEmeralds realEmeralds;
    private DataManager data;
    private Recipes recipes;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        data =  new DataManager(this);
        realEmeralds = new RealEmeralds(this);
        recipes = new Recipes(this, realEmeralds);
        getServer().getPluginManager().registerEvents(realEmeralds, this);
        getServer().getPluginManager().registerEvents(recipes,this);
        getServer().getPluginManager().registerEvents(new Listener(this, data, recipes),this);
        getCommand("realemerald").setExecutor(new Commands(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Real_emeralds disabled");
    }

}
