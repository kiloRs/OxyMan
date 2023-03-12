package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.data.StringData;
import net.Indyuce.mmoitems.stat.data.StringListData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryItem {
    private final MMOItem material;
    private final InventoryItemSettings settings;
    private ItemPriceData priceData;
    private double basePrice;

    public InventoryItem(String i){
        this(i,new InventoryItemSettings(1,0,1,3));
    }
    public InventoryItem(String i, InventoryItemSettings settings) {
        this.material = MMOItems.plugin.getMMOItem(Stats.DRUG, i);
        this.basePrice = settings.getBasePrice();
        if (material != null) {
            material.setData(Stats.currentPrice, new DoubleData(basePrice));
        } else {
            throw new RuntimeException("No Item Type: " + i);
        }

        this.settings = new InventoryItemSettings(basePrice,material.hasData(Stats.minPrice) ? ((DoubleData) material.getData(Stats.minPrice)).getValue() : 0, (material.hasData(Stats.maxPrice) ? ((DoubleData) material.getData(Stats.maxPrice)).getValue() : basePrice), OxyPlugin.getFileConfiguration().getInt("ItemSaveTimer", 10));

    }

    public String generateHoverMessage() {
        double currentPrice = getCurrentPrice();
        String hoverMessage = ChatColor.GOLD + "Current Price: " + currentPrice + " coins";
        if (currentPrice > basePrice) {
            hoverMessage += "\n" + ChatColor.RED + "Price is currently higher than base price.";
        } else if (currentPrice < basePrice) {
            hoverMessage += "\n" + ChatColor.GREEN + "Price is currently lower than base price.";
        }
        return hoverMessage;
    }

    public InventoryItem(String id, InventoryItemSettings settings, ItemPriceData priceData) {
        this.material = MMOItems.plugin.getMMOItem(Stats.DRUG,id);
        this.settings = settings;
        this.priceData = priceData;
    }
    public String getDisplayName() {
        return material.hasData(ItemStats.NAME) ? ((StringData) material.getData(ItemStats.NAME)).getString() : null;
    }

    public List<String> getLore() {
        return material.hasData(ItemStats.LORE) ? ((StringListData) material.getData(ItemStats.LORE)).getList() : new ArrayList<>();
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public ItemStack toItemStack(int amount) {
        ItemStack i = material.newBuilder().buildSilently();
        i.setAmount(amount);
        return i;
    }

    public String getId() {
        return material.getId();
    }

//    public double getCurrentPrice() {
//        double demandFactor = DemandFactors.getInstance().getFactor(material.getId());
//        double supplyFactor = SupplyFactors.getInstance().getFactor(material.getId());
//        double priceMultiplier = demandFactor / supplyFactor;
//        double newPrice = basePrice * priceMultiplier;
//        return Math.max(newPrice, settings.getMinimumPrice());
//    }
    public double getCurrentPrice(){
        return this.getCurrentPrice(InventoryItemManager.getElasticityAmount());
    }
    public double getCurrentPrice(double elasticity) {
        double supply = DemandFactors.getInstance(this.getId()).getFactor();
        double demand = SupplyFactors.getInstance(this.getId()).getFactor();

        // Calculate the current price using the supply and demand equation
        double currentPrice = basePrice * (1 + (supply / (demand + supply)) * elasticity);

        // Make sure the current price is within the minimum and maximum price range
        currentPrice = Math.max(this.settings.getMinimumPrice(), Math.min(this.settings.getMaximumPrice(), currentPrice));

        return currentPrice;
    }
    public InventoryItemSettings getSettings() {
        return settings;
    }

    public ItemMeta getItemSettings() {
        ItemMeta itemMeta = material.newBuilder().getMeta();
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        String currentPrice = ChatColor.GRAY + String.format("Current Price: %.2f", getCurrentPrice());
        String minPrice = ChatColor.GRAY + String.format("Minimum Price: %.2f", settings.getMinimumPrice());
        String maxPrice = ChatColor.GRAY + String.format("Maximum Price: %.2f", settings.getMaximumPrice());
        lore.set(2, currentPrice);
        lore.set(3, minPrice);
        lore.set(4, maxPrice);
        itemMeta.setLore(lore);
        return itemMeta;
    }

    public ItemPriceData getPriceData() {
        if (!material.hasData(Stats.startingCost) || !material.hasData(Stats.maxPrice) || !material.hasData(Stats.minPrice)){
            return new ItemPriceData(0,0,0);
        }

        return new ItemPriceData(((DoubleData) material.getData(Stats.startingCost)).getValue(), ((DoubleData) material.getData(Stats.minPrice)).getValue(), ((DoubleData) material.getData(Stats.maxPrice)).getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof InventoryItem that)) return false;

        return new EqualsBuilder().append(material.getId(), that.material.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(material.getId()).toHashCode();
    }
}