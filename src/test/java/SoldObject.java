//package com.thebetterchoice.oxyman.menu;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.Stats;
//import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
//import com.thebetterchoice.oxyman.menu.DrugSlot;
//import com.thebetterchoice.oxyman.menu.GUIAnalyzedSlot;
//import io.lumine.mythic.lib.api.item.NBTItem;
//import lombok.Getter;
//import org.bukkit.inventory.ItemStack;
//
//public class SoldObject {
//    private final String id;
//    private final RegisteredDrug drug;
//    @Getter
//    private final double amountSellingFor;
//    @Getter
//    private final double amount;
//    @Getter
//    private final GUIAnalyzedSlot slot;
//
//    public SoldObject(DrugSlot slot) {
//        this.id = slot.getId();
//        this.drug = slot.getRegisteredDrug();
//        this.amount =  slot.getAmount();
//        double value = NBTItem.get(slot.getItem()).getDouble(Stats.currentPrice.getNBTPath());
//        double sell = OxyPlugin.getMarket().sell(slot.getRegisteredDrug(), amount);
//        this.amountSellingFor = Math.max(value, sell);
//        this.slot = slot;
//    }
//
//    public ItemStack getItemStack(){
//        return slot.getItem();
//    }
//}
