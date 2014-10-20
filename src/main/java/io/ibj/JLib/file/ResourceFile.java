package io.ibj.JLib.file;

import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

/**
 * Created by Joe on 10/19/2014.
 */
public class ResourceFile {

    public ResourceFile(@NonNull JavaPlugin plugin, @NonNull String fileName) {
        this(plugin, new File(plugin.getDataFolder(), fileName));
    }
    public ResourceFile(@NonNull JavaPlugin plugin, File file) {
        if (!plugin.isEnabled())
            throw new IllegalArgumentException("Plugin must be enabled");
        this.plugin = plugin;
        this.resourceFile = file.getName();
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null)
            throw new IllegalStateException();
        this.configFile = file;
    }

    JavaPlugin plugin;
    String resourceFile;
    File configFile;
    Set<ResourceReloadHook> reloadHooks;

    public File getFile(){
        return configFile;
    }

    public void reloadConfig(){
        for(ResourceReloadHook hook : reloadHooks){
            hook.onReload(this);
        }
    }

    public void addReloadHook(ResourceReloadHook hook){
        reloadHooks.add(hook);
    }

}
