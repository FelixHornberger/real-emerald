package de.vendettagroup.real_emeralds.config;

import de.vendettagroup.real_emeralds.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private Main plugin;
    /*
    private FileConfiguration dataConfig;
    private File configFile;
     */

    private File directory;


    public DataManager(Main plugin) {
        this.plugin = plugin;
        directoryCheck();
        //saveDefaultConfig();
    }

    private void directoryCheck(){
        String directoryName = plugin.getDataFolder()+ File.separator +"blockPos";
        directory = new File(directoryName);
        if(!directory.mkdir()) {
            plugin.getLogger().info("Error couldnt creat directory");
        }
    }

    public void createBlockPosFile(String fileName, String blockname){
        File configFile =  new File(directory, fileName+".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
        yaml.set("blockName", blockname);
        try {
            yaml.save(configFile);
        } catch (IOException e){
            plugin.getLogger().log(Level.SEVERE, "Could not save config to" + configFile, e);
        }
    }

    public String checkIfBlockPosExists(String fileName) {
        File configFile =  new File(directory, fileName+".yml");
        if(configFile.exists()){
            FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);
            String toReturn = dataConfig.getString("blockName");
            configFile.delete();
            return toReturn;
        }
        return "error";
    }

}
