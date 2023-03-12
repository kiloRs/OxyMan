package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final File dataFolder;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
    }

    public static ConfigManager getInstance() {
        return new ConfigManager(((JavaPlugin) OxyPlugin.getPlugin()));
    }

    public FileConfiguration get(String fileName) {
        File file = new File(dataFolder, fileName);
        return YamlConfiguration.loadConfiguration(file);
    }

    public void save(FileConfiguration config, String fileName) {
        File file = new File(dataFolder, fileName);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(FileConfiguration config, String fileName, String header) {
        File file = new File(dataFolder, fileName);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String fileName) {
        File file = new File(dataFolder, fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String fileName, String resourcePath) {
        File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            plugin.saveResource(resourcePath, false);
        }
    }
}
