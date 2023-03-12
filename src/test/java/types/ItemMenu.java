//package com.thebetterchoice.oxyman.menu.types;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.menu.OxyInventory;
//import com.thebetterchoice.oxyman.menu.GUIAnalyzedSlot;
//import com.thebetterchoice.oxyman.sales.SellList;
//import com.thebetterchoice.oxyman.utils.NavigationReference;
//import com.thebetterchoice.oxyman.utils.ShopReference;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.event.inventory.InventoryOpenEvent;
//import org.bukkit.event.inventory.InventoryType;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ItemMenu extends OxyInventory {
//    private final Player player;
//    private final String shopName;
//    private final boolean influencesFrom;
//    private final Inventory inventory;
//    private final String shopRefName = "ItemMenu";
//
//    public ItemMenu(Player player) {
//        super(player);
//        this.player = player;
//        this.shopName = new ShopReference(shopRefName).getName();
//        this.influencesFrom = new ShopReference(shopRefName).isInfluences();
//        inventory = Bukkit.createInventory(this, InventoryType.CHEST, shopName);
//
//    }
//
//    @NotNull
//    @Override
//    public Inventory getInventory() {
//        return inventory;
//    }
//
//    @Override
//    public GUIAnalyzedSlot getSlot(int slot) {
//        return !getSlots().isEmpty()?getSlots().get(slot):null;
//    }
//
//    @Override
//    public void setSlot(GUIAnalyzedSlot analyzedSlot) {
//        getSlots().put(analyzedSlot.getSlot(),analyzedSlot);
//    }
//
//    @Override
//    public void setSlot(int slot, ItemStack itemStack) {
//        getInventory().setItem(slot,itemStack);
//    }
//
//    @Override
//    public void whenClicked(InventoryClickEvent ev) {
//        if (this.inventory==ev.getClickedInventory()){
//            loadInventoryToSpecialTypeMap(ev);
//        }
//    }
//
//    @Override
//    public void whenClosed(InventoryCloseEvent event) {
//        if (!(this.inventory==event.getInventory())) {
//            return;
//        }
//        loadInventoryToSpecialTypeMap(event);
//    }
//
//
//
//    @Override
//    public void whenRefresh(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//        OxyPlugin.log("Refresh System Event");
//
//    }
//
//    @Override
//    public void whenSell(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//        OxyPlugin.log("Sell System Event");
//
//    }
//
//    @Override
//    public void whenConfirm(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//        OxyPlugin.log("Confirm System Event");
//
//    }
//
//    @Override
//    public void whenViewPrice(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//    }
//
//    @Override
//    public void whenCancel(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        OxyPlugin.log("Cancel System Event");
//    }
//
//    @Override
//    public void whenHighToLow(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        SellList.sortDrugs(SellList.drugs,true);
//        loadInventoryToSpecialTypeMap(event);
//    }
//
//    @Override
//    public void whenLowToHigh(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        SellList.sortDrugs(SellList.drugs,false);
//        loadInventoryToSpecialTypeMap(event);
//    }
//
//    @Override
//    public boolean shouldOpen(InventoryOpenEvent openEvent, Map<Integer, GUIAnalyzedSlot> stored) {
//        if (!openEvent.isCancelled()){
//            return openEvent.getPlayer().hasPermission(new ShopReference(shopRefName).getPerm())&&setMenu();
//        }
//        return !openEvent.isCancelled();
//    }
//
//
//    private boolean setMenu(){
//        NavigationReference.loadItems(OxyPlugin.getNavigationConfig());
//        getInventory().setItem(getInventory().getSize()-1, NavigationReference.getSell());
//        getInventory().setItem(getInventory().getSize()-2, NavigationReference.getSell());
//        getInventory().setItem(getInventory().getSize()-3,NavigationReference.getFiller());
//        getInventory().setItem(getInventory().getSize()-4,NavigationReference.getFiller());
//        getInventory().setItem(getInventory().getSize()-5,NavigationReference.getFiller());
//        getInventory().setItem(getInventory().getSize()-6,NavigationReference.getFiller());
//        getInventory().setItem(getInventory().getSize()-7, NavigationReference.getCancel());
//        getInventory().setItem(getInventory().getSize()-8, NavigationReference.getLowToHigh());
//        getInventory().setItem(getInventory().getSize()-9, NavigationReference.getHighToLow());
//        OxyPlugin.debug("Inventory Set!");
//
//        return !this.inventory.isEmpty();
//    }
//    @Override
//    public Map<Integer, GUIAnalyzedSlot> getSlots() {
//        return new HashMap<>();
//    }
//
//    @Override
//    public void openInventory() {
//        player.openInventory(getInventory());
//        OxyPlugin.explain("Opening Inventory " + shopName,true);
//    }
//}
