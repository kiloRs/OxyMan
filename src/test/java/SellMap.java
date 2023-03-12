//package com.thebetterchoice.oxyman.menu;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.menu.DrugSlot;
//import com.thebetterchoice.oxyman.menu.GUIAnalyzedSlot;
//import lombok.Getter;
//import org.apache.commons.lang.builder.EqualsBuilder;
//import org.apache.commons.lang.builder.HashCodeBuilder;
//import org.bukkit.entity.Player;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class SellMap {
//    @Getter
//    private final Map<Integer,SoldObject> sellingMap = new HashMap<>();
//    private final Player player;
//    private final UUID uuid;
//    private double totalLoadValue = 0;
//    private boolean loaded;
//
//    public SellMap(OxyInventory inv){
//        player = inv.getPlayer();
//        uuid = inv.getPlayer().getUniqueId();
//
//        loadSelling(inv);
//
//    }
//
//    public void load(OxyInventory inventory){
//        loadSelling(inventory);
//        OxyPlugin.log("Load Attempted of SellMap");
//    }
//    private void loadSelling(OxyInventory inv) {
//        if (loaded){
//            OxyPlugin.debug("Cannot Load Inventory Selling Again.");
//            return;
//        }
//        Map<Integer, GUIAnalyzedSlot> slots = inv.getSlots();
//        calculateAll(slots);
//        loaded = true;
//    }
//
//    public void add(int slot, DrugSlot slotIcon){
//        sellingMap.put(slot,new SoldObject(slotIcon));
//    }
//    public void calculateAll(Map<Integer,GUIAnalyzedSlot> addAll){
//        for (Map.Entry<Integer, GUIAnalyzedSlot> entry : addAll.entrySet()) {
//            Integer integer = entry.getKey();
//            GUIAnalyzedSlot slot = entry.getValue();
//            if (!(slot instanceof DrugSlot drugSlot)){
//                OxyPlugin.explain("!! - Invalid Slot Type: " + slot.getSlot() + " " + slot.getOxyInventory().getPlayer().getDisplayName(),false);
//                continue;
//            }
//            SoldObject sold = new SoldObject(drugSlot);
//            sellingMap.put(integer, sold);
//            totalLoadValue = totalLoadValue + sold.getAmountSellingFor();
//            OxyPlugin.debug("Loading Value of " + sold.getAmountSellingFor() + " to " + totalLoadValue + " (total) of sale.");
//        }
//        OxyPlugin.log("Added " + addAll.size() + " drugs to sale!");
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (!(o instanceof SellMap sellMap)) return false;
//
//        return new EqualsBuilder().append(uuid, sellMap.uuid).isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37).append(uuid).toHashCode();
//    }
//}
