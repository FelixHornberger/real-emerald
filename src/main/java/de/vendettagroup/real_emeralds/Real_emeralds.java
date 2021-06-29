package de.vendettagroup.real_emeralds;

import org.bukkit.plugin.java.JavaPlugin;

public final class Real_emeralds extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new Emerald(), this);
        this.getLogger().info("Hopefully this is a good plugin, Real emeralds loaded");
    }

    @Override
    public void onDisable() {
        getLogger().info("Real_emeralds disabled");
    }
}
