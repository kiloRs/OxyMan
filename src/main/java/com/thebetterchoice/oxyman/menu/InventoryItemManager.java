package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InventoryItemManager {
    private static final List<InventoryItem> items = new ArrayList<>();
    private static final Map<String, InventoryItem> itemMap = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order

    public static void loadItems(JavaPlugin plugin) {
        items.clear();
        itemMap.clear();

        File itemsFile = new File(plugin.getDataFolder(), "items.yml");
        if (!itemsFile.exists()) {
            plugin.saveResource("items.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(itemsFile);
        for (String itemId : config.getKeys(false)) {
            double basePrice = config.getDouble(itemId + ".base-price");
            double maxPrice = config.getDouble(itemId + ".max-price");
            double minPrice = config.getDouble(itemId + ".min-price");
            int baseSupply = config.getInt(itemId + ".base-supply");

            InventoryItem item = new InventoryItem(itemId, new InventoryItemSettings(basePrice, maxPrice, minPrice, baseSupply));
            items.add(item);
            itemMap.put(itemId, item);
        }
    }

    public static void addItem(InventoryItem item) {
        items.add(item);
        itemMap.put(item.getId(), item);
    }

    public static InventoryItem getItem(String id) {
        return itemMap.get(id);
    }

    public static List<InventoryItem> getItems() {
        return new ArrayList<>(items); // Return a new ArrayList to prevent modification of the original list
    }

    public static InventoryItemSettings getItemSettings(String id) {
        return getItem(id).getSettings();
    }

    public static ItemPriceData getItemPriceInformation(String id) {
        return getItem(id).getPriceData();
    }

    public static double getElasticityAmount() {
        return OxyPlugin.getFileConfiguration().getDouble("elasticity",1.0);
    }

    public InventoryItem getSlot(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        } else {
            return null;
        }
    }
}
