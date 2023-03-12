package com.thebetterchoice.oxyman;

import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.stat.data.DoubleData;
import net.Indyuce.mmoitems.stat.type.DoubleStat;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import net.Indyuce.mmoitems.stat.type.StringListStat;
import net.Indyuce.mmoitems.stat.type.StringStat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

public class Stats {

    public static final ItemStat<?,?> news = new StringListStat("NEWS", Material.BOOK,"News",new String[]{""},new String[]{"NAVIGATION"});
    public static final ItemStat<?,?> startingCost = new DoubleStat("STARTING_COST",Material.EMERALD_BLOCK,"Starting Cost",new String[]{""},new String[]{"DRUG"});
    public static final ItemStat<?,?> startingDemand = new DoubleStat("STARTING_DEMAND",Material.EMERALD_ORE,"Starting amountOfItems", new String[]{},new String[]{"DRUG"});
    public static final ItemStat<?,?> minPrice = new DoubleStat("MIN_PRICE",Material.BARRIER,"Minimum Price", new String[]{},new String[]{"DRUG"});
    public static final ItemStat<?,?> maxPrice = new DoubleStat("MAX_PRICE",Material.BARRIER,"Maximum Price", new String[]{},new String[]{"DRUG"});

    public static final ItemStat<?,?> bonusType = new StringListStat("COST_BONUS_TYPE",Material.ENCHANTED_BOOK,"Cost Bonus Types",new String[]{""},new String[]{"DRUG"});
    public static final ItemStat<?,?> bonusInstance = new StringStat("COST_BONUS_INSTANCE",Material.KELP_PLANT,"Cost Bonus Instance",new String[]{""},new String[]{"ALL","!NAVIGATION"});
    public static final Type DRUG = MMOItems.plugin.getTypes().getOrThrow("DRUG");
    public static final Type NAVIGATION = MMOItems.plugin.getTypes().getOrThrow("NAVIGATION");
    public static final Type BONUSES = MMOItems.plugin.getTypes().getOrThrow("BONUSES");

    public static final ItemStat<?,?> currentPrice = new DoubleStat("CURRENT_PRICE",Material.ENCHANTED_GOLDEN_APPLE,"Current Price - Do Not Change",new String[]{"Please do not edit this!"},new String[]{"DRUG"});
    public static final ItemStat<?,?> view = new StringStat("VIEW",Material.GLASS,"View",new String[]{"Do not edit unless you know","what you're doing."},new String[]{"DRUG","NAVIGATION"});


    public Stats(){

        OxyPlugin.log("Loading Stats...");
    }
    public static Stats getInstance(){
        return new Stats();
    }


    public static double getPrice(RegisteredDrug registeredDrug) {
        double knownPrice = registeredDrug.getItem().hasData(currentPrice) ? ((DoubleData) registeredDrug.getItem().getData(currentPrice)).getValue() : registeredDrug.getCurrentCost();

        if (registeredDrug.getMaxValue() < knownPrice) {
            knownPrice = registeredDrug.getMaxValue();
        }
        else if (registeredDrug.getMinValue() > knownPrice){
            knownPrice = registeredDrug.getMinValue();
        }
            return knownPrice;
    }


    public static String getFormattedPrice(ItemStack itemStack){
        if (!NBTItem.get(itemStack).hasType()) {
            return null;
        }
        if (!NBTItem.get(itemStack).getType().equalsIgnoreCase(DRUG.getId())){
            return null;
        }
        if (!NBTItem.get(itemStack).hasTag(currentPrice.getNBTPath())){
            return null;
        }
        return OxyPlugin.format(getPrice(new RegisteredDrug(MMOItems.getID(itemStack))));
    }
    /**
     * @param registeredDrug The Drug to Check
     * @return A price, formatted as a nice, touchy string.
     */
    public static String getFormattedPrice(RegisteredDrug registeredDrug){
        return OxyPlugin.format(getPrice(registeredDrug));
    }


    /**
     * Registers all known stats here.
     */
    public void registerAll(){
        Field[] var1 = Stats.class.getFields();
        for (Field field : var1) {
            try {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && field.get(null) instanceof ItemStat itemStat) {
                    this.register(itemStat);
                    OxyPlugin.debug("Registered Stat: " + itemStat.getId());
                }
            } catch (IllegalAccessException | IllegalArgumentException var6) {
                OxyPlugin.throwError(var6.getMessage());
                OxyPlugin.debug("Error Registering Stats");
            }
        }
    }

    /**
     * @param d The Drug to Impact
     * @param value The Value to Assign to the Drug Type
     * @param storeRememberValue a UUID to remember the attachment?
     */
    public static void setPrice(RegisteredDrug d, double value, @Nullable UUID storeRememberValue){
        if (storeRememberValue == null){
            d.getItem().setData(currentPrice,new DoubleData(value));
            return;
        }
        d.getItem().mergeData(currentPrice,new DoubleData(value), storeRememberValue);
    }
    private void register(ItemStat<?,?> itemStat) {
        MMOItems.plugin.getStats().register(itemStat);
    }

    public void load() {
        registerAll();
    }
}
