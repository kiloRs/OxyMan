package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PriceConfig {

    private final Map<String, ItemPriceData> priceDataMap = new HashMap<String, ItemPriceData>();
    private final Map<String, Double> demandFactors = new HashMap<>();
    private final Map<String, Double> supplyFactors = new HashMap<>();
    private final File pricesFile;

    public PriceConfig () {
        this.pricesFile = new File(OxyPlugin.getPlugin().getDataFolder(),"prices.yml");
    }

    public void load() {
         YamlConfig config = new YamlConfig(pricesFile);
         config.load();

        // Load item prices
        priceDataMap.clear();
        for (String itemName : config.getKeys("prices")) {
            double basePrice = config.getDouble("prices." + itemName + ".base_price", 0);
            double minPrice = config.getDouble("prices." + itemName + ".min_price", 0);
            double maxPrice = config.getDouble("prices." + itemName + ".max_price", 0);
            double demandFactor = config.getDouble("prices." + itemName + ".demand_factor", 1);
            double supplyFactor = config.getDouble("prices." + itemName + ".supply_factor", 1);
            priceDataMap.put(itemName, new ItemPriceData(basePrice, minPrice, maxPrice, demandFactor, supplyFactor));
        }

        // Load demand factors
        demandFactors.clear();
        for (String itemName : config.getKeys("demand_factors")) {
            double demandFactor = config.getDouble("demand_factors." + itemName, 1);
            demandFactors.put(itemName, demandFactor);
        }

        // Load supply factors
        supplyFactors.clear();
        for (String itemName : config.getKeys("supply_factors")) {
            double supplyFactor = config.getDouble("supply_factors." + itemName, 1);
            supplyFactors.put(itemName, supplyFactor);
        }
    }

    public void save() {
        YamlConfig config = new YamlConfig(pricesFile);

        // Save item prices
        for (Map.Entry<String, ItemPriceData> entry : priceDataMap.entrySet()) {
            String itemName = entry.getKey();
            ItemPriceData priceData = entry.getValue();
            config.set("prices." + itemName + ".base_price", priceData.getBasePrice());
            config.set("prices." + itemName + ".min_price", priceData.getMinPrice());
            config.set("prices." + itemName + ".max_price", priceData.getMaxPrice());
            config.set("prices." + itemName + ".demand_factor", priceData.getDemandFactor());
            config.set("prices." + itemName + ".supply_factor", priceData.getSupplyFactor());
        }

        // Save demand factors
        for (Map.Entry<String, Double> entry : demandFactors.entrySet()) {
            String itemName = entry.getKey();
            double demandFactor = entry.getValue();
            config.set("demand_factors." + itemName, demandFactor);
        }

        // Save supply factors
        for (Map.Entry<String, Double> entry : supplyFactors.entrySet()) {
            String itemName = entry.getKey();
            double supplyFactor = entry.getValue();
            config.set("supply_factors." + itemName, supplyFactor);
        }

        config.save();
    }

    public ItemPriceData getPriceData(String itemName) {
        return priceDataMap.get(itemName);
    }

    public void setPriceData(String itemName, ItemPriceData priceData) {
        priceDataMap.put(itemName, priceData);
    }

    public double getDemandFactor(String itemName) {
        return demandFactors.getOrDefault(itemName, 1.0);
    }
}
