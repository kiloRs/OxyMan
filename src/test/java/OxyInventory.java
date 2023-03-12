//package com.thebetterchoice.oxyman.menu;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
//import com.thebetterchoice.oxyman.menu.slots.*;
//import com.thebetterchoice.oxyman.utils.SellUtil;
//import lombok.Getter;
//import net.Indyuce.mmoitems.MMOItems;
//import net.Indyuce.mmoitems.api.Type;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.event.inventory.InventoryEvent;
//import org.bukkit.event.inventory.InventoryOpenEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.InventoryHolder;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Map;
//
///**
// * Basic Inventory Holder/Listener
// *
// * All menu's of this plugin use this as thier super, meaning to look for a menu of this plugin, you simply do
// * getInventory().getHolder() instance of OxyInventory
// * on any inventory involved event.
// */
//public abstract class OxyInventory implements InventoryHolder  {
//
//    @Getter
//    private final Player player;
//    protected int page;
//
//
//
//    public OxyInventory(Player player) {
//        this.player = player;
//
//    }
//
//    public int getPage() {
//        return this.page;
//    }
//
//    public void loadInventoryToMap(InventoryEvent event, Map<Integer,GUIAnalyzedSlot> actualSlots){
//        getSlots().putAll(actualSlots);
//        if (getSlots().size()> getInventory().getSize()){
//            loadInventoryToSpecialTypeMap(event);
//            return;
//        }
//        OxyPlugin.debug("Error: Loading Actual Slots into Inventory Map - Invalid Size Error - Check Code in Oxy Inventory System.");
//    }
//    protected void loadInventoryToSpecialTypeMap(InventoryEvent event) {
//        if (getInventory().getSize()==getSlots().size()){
//            OxyPlugin.log("Skipping Slot Load: " + getInventory().getSize() + " found.");
//            return;
//        }
//
//        OxyPlugin.log("Running Inventory Slot Update to Check Slots...... ---->");
//        new BukkitRunnable(){
//            @Override
//            public void run() {
//
//
//                for (int i = 0; i < getInventory().getSize(); i++) {
//                    ItemStack item = getInventory().getItem(i);
//                    Type type = SellUtil.getType(item);
//                    SellUtil.SellType sellType = SellUtil.findSellType(type);
//                    switch (sellType){
//
//                        case DRUG -> {
//                            RegisteredDrug validForSale = SellUtil.isValidForSale(item);
//                            if (validForSale == null){
//                                throw new RuntimeException("No Known Item in Drugs");
//                            }
//                            getSlots().put(i,new DrugSlot(validForSale,item, OxyInventory.this,i,item.getAmount()));
//                        }
//                        case NAVIGATION -> {
//                            RegisteredDrug validForSale = SellUtil.isValidForSale(item);
//                            if (validForSale == null){
//                                throw new RuntimeException("No Known Item in Navigation");
//                            }
//                            getSlots().put(i,new NavigationSlot(i,item,new Navigation(MMOItems.getID(item)),OxyInventory.this,item.getAmount()));
//
//                        }
//                        case VANILLA -> {
//                            if (item == null){
//                                getSlots().put(i,new EmptySlot(i,OxyInventory.this));
//                                return;
//                            }
//                            getSlots().put(i,new VanillaSlot(item,i,OxyInventory.this,item.getAmount()));
//                        }
//                        case NONE, BONUSES, UNKNOWN -> {
//                            getSlots().put(i,new EmptySlot(i,OxyInventory.this));
//
//                        }}}
//            }
//        }.runTaskAsynchronously(OxyPlugin.getPlugin());
//        OxyPlugin.log("Running Bukkit Runnable for Organizing Slots for " + event.getEventName());
//    }
//    public abstract @NotNull Inventory getInventory();
//
//    public abstract GUIAnalyzedSlot getSlot(int slot);
//
//    public abstract void setSlot(GUIAnalyzedSlot analyzedSlot);
//
//    public abstract void setSlot(int slot, ItemStack itemStack);
//
//    public abstract void whenClicked(InventoryClickEvent ev);
//
//
//    public abstract void whenClosed(InventoryCloseEvent event);
//
//
//    public void setPage(int pageToGo){
//        this.page = pageToGo;
//    }
//
//    public abstract void whenRefresh(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract void whenSell(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract void whenConfirm(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract void whenViewPrice(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract void whenCancel(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract void whenHighToLow(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract void whenLowToHigh(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap);
//
//    public abstract boolean shouldOpen(InventoryOpenEvent openEvent, Map<Integer, GUIAnalyzedSlot> stored);
//
//    public abstract Map<Integer,GUIAnalyzedSlot> getSlots();
//
//    public abstract void openInventory();
//
//    public boolean open() {
//        boolean primaryThread = Bukkit.isPrimaryThread();
//
//        if (primaryThread){
//            OxyPlugin.debug("Primary Thread Error.");
//            return false;
//        }
//
//        getPlayer().openInventory(getInventory());
//        OxyPlugin.debug("Attempting to open inventory...");
//        return true;
//    }
//    }
