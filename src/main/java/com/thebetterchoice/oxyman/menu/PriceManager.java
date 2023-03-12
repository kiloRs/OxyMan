package com.thebetterchoice.oxyman.menu;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PriceManager {
    private static final String PRICES_FILE_NAME = "prices.yml";
    private static final long SAVE_INTERVAL_MINUTES = 30;

    private final Plugin plugin;
    private final File pricesFile;
    private final FileConfiguration pricesConfig;
    private final Map<String, PriceData> prices;

    public PriceManager(Plugin plugin) {
        this.plugin = plugin;
        pricesFile = new File(plugin.getDataFolder(), PRICES_FILE_NAME);
        pricesConfig = YamlConfiguration.loadConfiguration(pricesFile);
        prices = new HashMap<>();

        // Schedule auto-save
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::savePrices, 0,
                TimeUnit.MINUTES.toSeconds(SAVE_INTERVAL_MINUTES));
    }

    public void loadPrices() {
        if (!pricesFile.exists()) {
            plugin.getLogger().info(PRICES_FILE_NAME + " not found, creating new file.");
            try {
                pricesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to create " + PRICES_FILE_NAME + ": " + e.getMessage());
            }
            return;
        }

        // Load prices from file
        for (InventoryItem item: InventoryItemManager.getItems()) {
            String itemId = item.getId();
            double price = pricesConfig.getDouble(itemId, -1);
            double minPrice = InventoryItemManager.getItem(itemId).getPriceData().getMinPrice();
            double maxPrice = InventoryItemManager.getItem(itemId).getPriceData().getMaxPrice();
            if (price < minPrice || price > maxPrice) {
                price = InventoryItemManager.getItem(itemId).getPriceData().getBasePrice();
            }
            int sales = pricesConfig.getInt(itemId + ".sales", 0);
            int supply = pricesConfig.getInt(itemId + ".supply", 0);
            int demand = pricesConfig.getInt(itemId + ".demand", 0);
            prices.put(itemId, new PriceData(price, sales, supply, demand));
        }
        plugin.getLogger().info("Loaded " + prices.size() + " item prices from " + PRICES_FILE_NAME);
    }

    public double getPrice(String itemId) {
        return prices.getOrDefault(itemId, new PriceData()).getPrice();
    }

    public void setPrice(String itemId, double price) {
        InventoryItem item = InventoryItemManager.getItem(itemId);
        double minPrice = item.getPriceData().getMinPrice();
        double maxPrice = item.getPriceData().getMaxPrice();
        if (price < minPrice) {
            price = minPrice;
        } else if (price > maxPrice) {
            price = maxPrice;
        }
        prices.put(itemId, new PriceData(price, getSales(itemId), SupplyFactors.getInstance(itemId), DemandFactors.getInstance(itemId)));
    }

    public void incrementSales(String itemId) {
        prices.computeIfPresent(itemId, (id, priceData) ->
                new PriceData(priceData.getPrice(), priceData.getSales() + 1, priceData.getSupply(), priceData.getDemand()));
    }

    public int getSales(String itemId) {
        return prices.getOrDefault(itemId, new PriceData()).getSales();
    }

    public void setSupply(String itemId, int supply) {
        prices.computeIfPresent(itemId, (id, priceData) ->
                new PriceData(priceData.getPrice(), priceData.getSales(), supply, priceData.getDemand()));
    }

}
