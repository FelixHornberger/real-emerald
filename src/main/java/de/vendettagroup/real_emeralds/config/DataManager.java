package de.vendettagroup.real_emeralds.config;
import de.vendettagroup.real_emeralds.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DataManager {

    private Main plugin;

    private File directory;


    public DataManager(Main plugin) {
        this.plugin = plugin;
        directoryCheck();
        checkForLoreLog();
    }

    private void directoryCheck(){
        String directoryName = plugin.getDataFolder() + File.separator +"blockPos";
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

    private void checkForLoreLog(){
        File loreLogFile =  new File(plugin.getDataFolder(), "loreLog.yml");
        if (!loreLogFile.exists()){
            clearLoreLog();
        }
    }

    private void createLoreLog() {
        File loreLogFile =  new File(plugin.getDataFolder(), "loreLog.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(loreLogFile);
        List<String> lorList = new ArrayList<String>();
        lorList.add(plugin.getConfig().getString("mEmerald.lore"));
        yaml.set("loreLog", lorList);
        try {
            yaml.save(loreLogFile);
        } catch (IOException e){
            plugin.getLogger().log(Level.SEVERE, "Could not save config to" + loreLogFile, e);
        }
    }

    public void checkIfLoreIsAlreadyInLog(){
        File loreLogFile =  new File(plugin.getDataFolder(), "loreLog.yml");
        if(loreLogFile.exists()){
            FileConfiguration loreLogConfig = YamlConfiguration.loadConfiguration(loreLogFile);
            List<String> lorList = loreLogConfig.getStringList("loreLog");
            for (String element : lorList){
                if(element.equals(plugin.getConfig().getString("mEmerald.lore"))) {
                    return;
                }
            }
            lorList.add(plugin.getConfig().getString("mEmerald.lore"));
            loreLogConfig.set("loreLog", lorList);
            try {
                loreLogConfig.save(loreLogFile);
            } catch (IOException e){
                plugin.getLogger().log(Level.SEVERE, "Could not save config to" + loreLogFile, e);
            }
        }
    }

    public List<String> getLoreLog(){
        File loreLogFile =  new File(plugin.getDataFolder(), "loreLog.yml");
        if(loreLogFile.exists()){
            FileConfiguration loreLogConfig = YamlConfiguration.loadConfiguration(loreLogFile);
            return loreLogConfig.getStringList("loreLog");
        }
        return null;
    }

    public void clearLoreLog(){
        File loreLogFile =  new File(plugin.getDataFolder(), "loreLog.yml");
        if(loreLogFile.exists()){
            loreLogFile.delete();
        }
        createLoreLog();
    }

}
