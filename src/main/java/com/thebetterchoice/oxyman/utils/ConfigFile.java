package com.thebetterchoice.oxyman.utils;

import com.jeff_media.jefflib.ConfigUtils;
import com.thebetterchoice.oxyman.OxyPlugin;
import net.Indyuce.mmoitems.manager.Reloadable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigFile extends net.Indyuce.mmoitems.api.ConfigFile implements Reloadable {
    private static Plugin plugin = OxyPlugin.getPlugin();
    private final String path;
    private final String name;
    private final File file;
    private boolean replace;

    public ConfigFile(String name) {
        super(plugin,name);
        this.name = name;
        this.path = "/";
        file = new File(plugin.getDataFolder(), name + ".yml");
        ConfigUtils.getConfig(name + ".yml");

        if (!this.exists()) {
//            this.getConfig().options().copyDefaults(true).pathSeparator('.').header("Created by Kilo! Thank you for your business!");
//            this.getConfig().save(new File(plugin.getDataFolder(), name + ".yml"));
//            load();
            checkFile();
            save();
        }

    }

    public ConfigFile(Plugin plugin, String name) {
        super(plugin, name);
        ConfigFile.plugin = plugin;
        this.name = name;
        this.path = "/";
        file = new File(ConfigFile.plugin.getDataFolder(), name + ".yml");

        if (!this.exists()) {
//            load();
            checkFile();

//            this.getConfig().options().copyDefaults(true).pathSeparator('.').header("Created by Kilo! Thank you for your business!");
//            this.getConfig().save(new File(ConfigFile.plugin.getDataFolder(), name + ".yml"));
            save();
        }

    }

    public ConfigFile(String path, String name) {
        super(path, name);
        this.path = path;
        this.name = name;
        plugin = OxyPlugin.getPlugin();
        file = new File(plugin.getDataFolder(), name + ".yml");

        if (!this.exists()) {
//            this.getConfig().options().copyDefaults(true).pathSeparator('.').header("Created by Kilo! Thank you for your business!");
//            this.getConfig().save(new File(plugin.getDataFolder(), name + ".yml"));
            checkFile();

            save();
        }
    }

    public ConfigFile(Plugin plugin, String path, String name) {
        super(plugin, path, name);
        ConfigFile.plugin = plugin;
        this.path = path;
        this.name = name;
        file = new File(ConfigFile.plugin.getDataFolder(), name + ".yml");

        if (!this.exists()) {
            checkFile();
//            this.getConfig().options().copyDefaults(true).pathSeparator('.').header("Created by Kilo! Thank you for your business!");

            save();
        }
    }

    @Override
    public void reload() {

        if (!this.exists()) {
            checkFile();
        }
    }
    public String getName() {
        return name;
    }

    public File getFile(){
        return file;
    }

    public void checkFile() {
        if (file.getName().contains("config") && !file.isFile()){
            OxyPlugin.getPlugin().saveDefaultConfig();
            return;
        }
        else if (file.isFile()){
            OxyPlugin.getPlugin().saveConfig();
            return;
        }
        if (!file.exists()) {
            InputStream defaultConfig = OxyPlugin.getPlugin().getResource(name+".yml");
            if (defaultConfig == null){
                throw new RuntimeException("Bad Config");
            }
            getConfig().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultConfig)));
            getConfig().options().copyDefaults(true); //Don't know whether you have to do this...return;

        }
        OxyPlugin.log("Loaded File " + name );

    }
}

