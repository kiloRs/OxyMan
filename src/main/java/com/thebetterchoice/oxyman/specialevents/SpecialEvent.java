package com.thebetterchoice.oxyman.specialevents;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import io.lumine.mythic.lib.MythicLib;
import lombok.Getter;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SpecialEvent  {
    protected static final List<RegisteredDrug> items = new ArrayList<>();
    private static final List<ItemStack> effectedVanilla = new ArrayList<>();
    private String name;
    @Getter
    private String display = "";
    @Getter
    private double modifier;
    @Getter
    private double weight;
    @Getter
    private List<RegisteredDrug> affects;
    @Getter
    private double chance;
    @Getter
    private boolean anyItem;
    @Getter
    private List<ItemStack> vanilla;


    public SpecialEvent(String eventName) {
        if (eventName == null || eventName.isEmpty()){
            OxyPlugin.log("Error with event name! " + eventName);
            return;
        }
        this.name = eventName;
        this.affects = new ArrayList<>();
        this.vanilla = new ArrayList<>();

        this.chance = SpecialEventUtil.getChance(eventName);
        this.modifier = SpecialEventUtil.getModifier(eventName);
        this.weight = SpecialEventUtil.getWeight(eventName);
        this.display = SpecialEventUtil.getDisplay(eventName);

        List<String> effects = SpecialEventUtil.getEffected(eventName);
        for (String effect : effects) {
            if (effect.equalsIgnoreCase("all")){
                continue;
            }
            MMOItem mmoItem = MMOItems.plugin.getMMOItem(Stats.DRUG, effect);
            if (mmoItem == null){
                OxyPlugin.log("No MMOItem found for " + effect.toUpperCase(Locale.ROOT));
                continue;
            }
            if (!DrugRegistry.hasDrug(mmoItem)){
                OxyPlugin.log("Registered LoadingDrug not found! [" + effect + "]");
                continue;
            }
            RegisteredDrug drug = DrugRegistry.getRegisteredDrugs().get(mmoItem.getId());
            if (drug == null){
                OxyPlugin.log("Invalid LoadingDrug: " + effect + "for effects of " + eventName);
                continue;
            }
            this.affects.add(drug);
            OxyPlugin.log("Added LoadingDrug: " + effect + "for effects of " + eventName);
        }



        OxyPlugin.log("Loaded Special Event With No Effects + " + "[" + name + "]");

        anyItem = this.affects.isEmpty();
    }
    public SpecialEvent(String name, List<RegisteredDrug> items) {
        this(name);
        vanilla = new ArrayList<>();
        if (!this.affects.containsAll(items)){
            this.affects = items;
            return;
        }
        OxyPlugin.log("Loaded Special Event With Effects + " + "[" + name + "]");


    }

    public SpecialEvent(String s, List<RegisteredDrug> items, List<ItemStack> effectedVanilla) {
        this(s,items);
        vanilla = effectedVanilla;

        OxyPlugin.log("Loaded Special Event With Effects + " + "[" + name + "]");

    }


//    private boolean saveEvent() {
//        return SpecialEventUtil.saveEach(this.name);
//    }
//
//    /**
//     * @param o Currently only supports collections of strings, more to come.
//     * @return if it saves EVERY one.
//     */
//    public static boolean saveEventsFrom(Object o){
//        int xFactor = 0;
//        List<String> values = new ArrayList<>();
//        if (o instanceof Collection<?> collection){
//            if (!collection.isEmpty()){
//                for (Object keys : collection) {
//                    if (keys instanceof String s){
//
//                        values.add(s);
//                    }
//            }
//            }
//        }
//        if (values.isEmpty()){
//            return false;
//        }
//        xFactor = values.size() -1;
//        for (String value : values) {
//            SpecialEventUtil.saveEach(value);
//        }
//        return xFactor==0;
//    }
//    private static boolean saveEvents(List<SpecialEvent> specialEvents) {
//        int xFactor = specialEvents.size()-1;
//        for (SpecialEvent eachEvent : specialEvents) {
//            if (SpecialEventUtil.saveEach(eachEvent.name)) {
//                xFactor--;
//            }
//
//        }
//        return xFactor==0;
//        if (section == null){
//            section = OxyPlugin.getFileConfiguration().createSection("events." + this.name));
//        }
//        if (!section.getKeys(false).contains("example")){
//            section.set("name",this.name);
//            section.set("modifier",this.modifier);
//            section.set("weight",this.weight);
//            section.set("chance",this.chance);
//            ArrayList<String> list = new ArrayList<>();
//            list.add("COCAINE");
//            list.add("AMPHETAMINES");
//            list.add("SPEED");
//            section.set("effects", list);
//            OxyPlugin.getPlugin().saveConfig();
//            return true;
//        }
//        return false;

    @NotNull
    public static List<SpecialEvent> parseEvents(ConfigurationSection events) {
        Set<String> k = events.getKeys(false);
        ArrayList<SpecialEvent> specialEvents = new ArrayList<>();

        if (k.isEmpty()){
            throw new RuntimeException("No Events Found Within Configuration!");
        }

        for (String s : k) {
            SpecialEvent specialEvent = new SpecialEvent(s);

            List<String> effectList = SpecialEventUtil.getEffected(s) ;
            if (effectList.isEmpty() || effectList.size() == 1 && effectList.get(0).equalsIgnoreCase("all")) {
                specialEvents.add(specialEvent);
                OxyPlugin.debug("No Effects found for " + s + " or set to 'all'");
                continue;
            }

                for (String effect : effectList) {
                    if (effect.startsWith("!")){
                        effect = effect.split("!")[1];
                    }
                    else {
                        if (!effect.isEmpty()){
                            Material material = Material.getMaterial(effect);
                            if (material == null){
                                continue;
                            }
                            ItemStack itemStack = new ItemStack(material);
                        }
                    }
                    MMOItem itemFound = MMOItems.plugin.getMMOItem(Stats.DRUG, effect);
                    if (itemFound == null){
                        continue;
                    }

                    if (!DrugRegistry.hasDrug(itemFound)){
                        continue;
                    }
                    RegisteredDrug drug = DrugRegistry.getRegisteredDrugs().get(itemFound.getId());
                    items.add(drug);
                }



            if (items.isEmpty()){
                OxyPlugin.log("Loading Special Event from Config pulled 0 items.");
            }
            else {
                OxyPlugin.log("Loading Special Event from Config pulled"+ items.size() +" items.");
            }
            SpecialEvent event = new SpecialEvent(s, items,effectedVanilla);
            specialEvents.add(event);
            if (specialEvents.contains(event)) {
                Bukkit.getConsoleSender().sendMessage(MythicLib.plugin.parseColors("&aLocated Event: &e" + event.getPath() + "&a!"));
            }
        }
        items.clear();
        return specialEvents;
    }




    public String getPath() {
        if (!name.contains("event")){
            return name;
        }
        return name.split("event")[0];
    }

}
