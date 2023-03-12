package com.thebetterchoice.oxyman.drugs;


import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.Indyuce.mmoitems.api.item.template.MMOItemTemplate;
import net.Indyuce.mmoitems.manager.Reloadable;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Holds drug instances, registers drugs.
 */
public class DrugRegistry implements Reloadable {
    @Getter
    private static final Map<String, RegisteredDrug> registeredDrugs = new HashMap<>();
    @Getter
    private static final Map<Material, Double> vanillaValues = new HashMap<>();


    @Getter
    private final Plugin plugin;

    public DrugRegistry(Plugin plugin) {
        this.plugin = plugin;

//
//            for (String knownDrug : knownDrugs) {
//                OxyPlugin.getDrugsConfiguration().getConfig().options().header("The base price of drugs is set through MMOItems by giving them the StartingCost stat! Also you can give the starting amountOfItems stat to the drugs in MMOItems!");
//                OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Price.Max",-1);
//                OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Price.Min",0);
//                OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Demand.Base",1.0);
//                OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Type",new String[]{});
//                //Load Configurations
//            }
//            OxyPlugin.getDrugsConfiguration().save();
//            OxyPlugin.log("Saved LoadingDrug Configurations!");
//
//        }


        registerDrugsAs();

    }


    public static void register(String type, String id) {
        register(type + " " + id);
    }

    public static void register(String knownDrugFull) {
        String knownDrug = knownDrugFull.split(" ")[1];
        String typeN = knownDrugFull.split(" ")[0];

        MMOItemTemplate drug = new MMOItemTemplate(MMOItems.plugin.getTypes().has(typeN) ? MMOItems.plugin.getTypes().get(typeN) : Stats.DRUG, knownDrug);

        if (!MMOItems.plugin.getTemplates().hasTemplate(MMOItems.plugin.getTypes().has(typeN) ? MMOItems.plugin.getTypes().get(typeN) : Stats.DRUG, knownDrug)) {
            MMOItems.plugin.getTemplates().registerTemplate(drug);
            OxyPlugin.debug("Registered Template: " + drug.getId());
        }

//        if (!OxyPlugin.getDrugsConfiguration().getConfig().isConfigurationSection(knownDrug)){
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Options.Display",false);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Options.Remove",false);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Bonus.Types","UPPER");
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Price.Base",10);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Price.Max",-1);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Price.Min",0);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Demand.Base",1.0);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Demand.Max",-1);
//            OxyPlugin.getDrugsConfiguration().getConfig().set(knownDrug + ".Type",new String[]{});
//
//            OxyPlugin.getDrugsConfiguration().save();
//        }

//        int maxPrice = (int) ConfigReference.serverMaxCost;
//        int minPrice = (int) ConfigReference.serverMinCost;
//        double baseDemand = Math.max(OxyPlugin.getDrugsConfiguration().getConfig().getDouble(knownDrug + ".Demand.Base", ConfigReference.serverDemandBase), ConfigReference.serverDemandBase);
//        double demandMax = ConfigReference.serverDemandMax;
//        double basePrice = Math.max(OxyPlugin.getDrugsConfiguration().getConfig().getDouble(knownDrug + ".Price.Base", ConfigReference.serverBaseCost), ConfigReference.serverBaseCost);

//        drug.getBaseItemData().put(Stats.startingDemand, new NumericStatFormula(baseDemand));
//        drug.getBaseItemData().put(Stats.startingCost, new NumericStatFormula(basePrice));

//        basePrice =  fitPriceIntoHold(maxPrice, minPrice, basePrice);
//        drug.newBuilder().applyData(Stats.startingCost,new DoubleData(basePrice));
//        if (baseDemand>demandMax){
//            baseDemand = demandMax;
//        }
//        drug.newBuilder().applyData(Stats.startingDemand,new DoubleData(baseDemand));
        RegisteredDrug registeredDrug = new RegisteredDrug(drug.getId());

        registeredDrugs.put(drug.getId(), registeredDrug);
//        if (!demand.containsKey(registeredDrug)) {
//            demand.put(registeredDrug, registeredDrug.getStartingDemand());
//        }

        OxyPlugin.debug("Loaded Registered Drug: " + knownDrug);
    }

    public static void loadItems() {
        addItems().runTaskTimerAsynchronously(OxyPlugin.getPlugin(), 1200 * ((long) ConfigReference.itemLoadStartLength), ConfigReference.itemLoadStartLength * (long) 1200);
    }

    private static double fitPriceIntoHold(int maxPrice, int minPrice, double basePrice) {
        if (basePrice > maxPrice && maxPrice != -1) {
            basePrice = maxPrice;
        }
        if (basePrice < minPrice && minPrice != -1) {
            basePrice = minPrice;
        }
        if (basePrice > ConfigReference.serverMaxCost && ConfigReference.serverMaxCost != -1) {
            basePrice = ConfigReference.serverMaxCost;
        }
        if (basePrice < ConfigReference.serverMinCost && ConfigReference.serverMinCost != -1) {
            basePrice = ConfigReference.serverMinCost;
        }
        return basePrice;
    }

    public static boolean hasDrug(String id) {
        MMOItem mmoItem = MMOItems.plugin.getMMOItem(Stats.DRUG, id);
        if (mmoItem == null) {
            if (MMOItems.plugin.getTemplates().hasTemplate(Stats.DRUG, id)) {
                return false;
            }
            MMOItemTemplate mmoTemplate = new MMOItemTemplate(Stats.DRUG, id);
            MMOItems.plugin.getTemplates().registerTemplate(mmoTemplate);

            mmoItem = MMOItems.plugin.getMMOItem(Stats.DRUG, id);
            if (mmoItem == null) {
                throw new RuntimeException("Bad MMOItem in HasDrug Method");
            }
        }
        return hasDrug(mmoItem);
    }

    public static boolean hasDrug(MMOItem n) {
        return registeredDrugs.containsKey(n.getId()) && registeredDrugs.get(n.getId()) != null;
    }

    @NotNull
    public static BukkitRunnable addItems() {
        return new BukkitRunnable() {
            @Override
            public void run() {

                Set<String> keys = OxyPlugin.getDrugsConfiguration().getConfig().getKeys(false);
                if (!keys.isEmpty()) {
                    for (String eachKey : keys) {
                        MMOItemTemplate mmoTemplate = new MMOItemTemplate(Stats.DRUG, eachKey);
                        if (!MMOItems.plugin.getTemplates().hasTemplate(Stats.DRUG, eachKey)) {
                            MMOItems.plugin.getTemplates().registerTemplate(mmoTemplate);

                        }
                        if (registeredDrugs.containsKey(eachKey)) {
                            RegisteredDrug r = registeredDrugs.get(eachKey);
//                            demand.put(r, r.getDrug().getDemand());
                            return;
                        }
                        if (OxyPlugin.isDebug()) {
                            OxyPlugin.debug("Registering Item: " + eachKey);
                        }
                        RegisteredDrug p = new RegisteredDrug(mmoTemplate.getId());
                        registeredDrugs.put(eachKey, p);
//                        demand.put(p, p.getDrug().getDemand());
                    }
                }

            }
        };
    }

    public static Map<Material, Double> getVanillaPrices() {
        return vanillaValues;
    }

    private void registerDrugsAs() {

        Set<String> keys = OxyPlugin.getDrugsConfiguration().getConfig().getKeys(false);
        if (keys.isEmpty()) {
            throw new RuntimeException("No Drug Keys");
        }
        Type drugType = Stats.DRUG;
        for (String eachDrug : keys) {
            if (eachDrug.isEmpty()) {
                continue;
            }
            register(drugType.getId(), eachDrug.toUpperCase(Locale.ROOT));
        }
        //Registers Each LoadingDrug as MMOItem For Sure


    }

    public void postLoad() {
        Collection<MMOItemTemplate> mmoItemTemplates = MMOItems.plugin.getTemplates().collectTemplates();
        List<MMOItemTemplate> fresh = new ArrayList<>();
        for (MMOItemTemplate mmoItemTemplate : mmoItemTemplates) {
            if (mmoItemTemplate.getType() != Stats.DRUG) {
                continue;
            }
            fresh.add(mmoItemTemplate);
        }

        Type drug = Stats.DRUG;
        for (MMOItemTemplate template : fresh) {

            if (template.getType() != drug) {
                fresh.remove(template);
                continue;
            }

            if (DrugRegistry.hasDrug(template.newBuilder().build()) && DrugRegistry.getRegisteredDrugs().containsKey(template.newBuilder().build().getId())) {
                continue;
            }


            DrugRegistry.register(template.getType().getId(), template.getId());
        }


    }


    @Override
    public void reload() {
        postLoad();
    }
}
