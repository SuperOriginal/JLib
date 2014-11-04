package io.ibj.JLib.file;

import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Created by Joe on 10/19/2014.
 */
public class YAMLFile extends ResourceFile {
    public YAMLFile(@NonNull JavaPlugin plugin, @NonNull String fileName, ResourceReloadHook... reloadHooks) {
        super(plugin, fileName,reloadHooks);
    }

    public YAMLFile(@NonNull JavaPlugin plugin, @NonNull File file, ResourceReloadHook... reloadHooks){
        super(plugin,file, reloadHooks);
    }

    FileConfiguration config;

    @Override
    public void reloadConfig() {
        if(!getFile().exists()){
            saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(getFile());
        InputStream defConfigStream = plugin.getResource(resourceFile);
        if (defConfigStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(defConfigStream);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(inputStreamReader);
            config.setDefaults(defConfig);
        }
        super.reloadConfig();
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(resourceFile, false);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            this.reloadConfig();
        }
        return config;
    }

}
