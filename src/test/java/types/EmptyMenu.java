//package com.thebetterchoice.oxyman.menu.types;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.OxyUser;
//import com.thebetterchoice.oxyman.drugs.DrugRegistry;
//import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
//import com.thebetterchoice.oxyman.menu.OxyInventory;
//import com.thebetterchoice.oxyman.menu.GUIAnalyzedSlot;
//import com.thebetterchoice.oxyman.utils.ShopReference;
//import io.lumine.mythic.lib.api.item.NBTItem;
//import net.Indyuce.mmoitems.api.Type;
//import org.apache.commons.lang.builder.EqualsBuilder;
//import org.apache.commons.lang.builder.HashCodeBuilder;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
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
//import java.util.UUID;
//import java.util.concurrent.Callable;
//
//import static com.thebetterchoice.oxyman.utils.SellUtil.getType;
//
//public class EmptyMenu extends OxyInventory {
//
//    private final Player player;
//    private final UUID uuid;
//    private final Inventory inventory = Bukkit.createInventory(this, InventoryType.ENDER_CHEST, "Sell Station");
//    private final HashMap<Integer, GUIAnalyzedSlot> slotMap = new HashMap<>();
//    private String shopRefName;
//
//    public EmptyMenu(Player player){
//        super(player);
//        this.shopRefName = "EmptyShop";
//        this.player = player;
//        this.uuid = player.getUniqueId();
//    }
//
//    public EmptyMenu(OxyUser oxyUser){
//        this(oxyUser.getPlayer());
//    }
//    @NotNull
//    @Override
//    public Inventory getInventory() {
//        return inventory;
//    }
//
//    @Override
//    public GUIAnalyzedSlot getSlot(int slot) {
//        return getSlots().get(slot);
//    }
//
//    @Override
//    public void setSlot(GUIAnalyzedSlot analyzedSlot) {
//
//    }
//
//    @Override
//    public void setSlot(int slot, ItemStack itemStack) {
//        inventory.setItem(slot,itemStack);
//    }
//
//    @Override
//    public void whenClicked(InventoryClickEvent ev) {
//
//        loadInventoryToSpecialTypeMap(ev);
//
//        boolean cancelled;
//        OxyPlugin.debug("Click from Empty Menu - Through Oxy Inventory");
//        if (!(ev.getView().getTopInventory().getHolder() instanceof OxyInventory oxyInventory)){
//            return;
//        }
//        if (ev.getView().getTopInventory() != this.getInventory()){
//            return;
//        }
//
//        int page = oxyInventory.getPage();
//        OxyPlugin.log("Oxy Inventory for " + ev.getWhoClicked().getName() + " on page " + page );
//
//
//        Map<RegisteredDrug, Integer> sellMap = DrugRegistry.getSales();
//
//        if (!(ev.getView().getTopInventory().getHolder() instanceof EmptyMenu e)){
//            ev.setResult(Event.Result.DENY);
//            return;
//        }
//        OxyPlugin.log("Empty Holder for " + ev.getWhoClicked().getName());
//
//        ItemStack current = ev.getCurrentItem();
//            if (ev.getView().getTopInventory() == new EmptyMenu(((Player) ev.getWhoClicked())).getInventory()) {
//               OxyPlugin.log("Empty Menu Found for " + ev.getWhoClicked().getName());
//                Inventory bottom = ev.getView().getBottomInventory();
//                NBTItem n = NBTItem.get(current);
//                String id = n.getString("MMOITEMS_ITEM_ID");
//                Type type = getType(current);
//                if (current == null) {
//                    ev.setResult(Event.Result.DENY);
//                    return;
//                }
//                if (type == null) {
//                    ev.setResult(Event.Result.DENY);
//                    return;
//                }
//                Inventory clickedInv = ev.getClickedInventory();
//                if (clickedInv == null){
//                    throw new RuntimeException("Null Inv Click");
//                }
//                if (!DrugRegistry.getRegisteredDrugs().containsKey(id) && bottom==clickedInv) {
//                    ev.setResult(Event.Result.DENY);
//                    return;
//                }
//                RegisteredDrug key = DrugRegistry.getRegisteredDrugs().get(id);
//
//                if (!(clickedInv.getHolder() instanceof EmptyMenu) && ev.getView().getTopInventory().getHolder() instanceof EmptyMenu emptyMenu) {
//                    OxyPlugin.debug("Inv Option 1");
//
//                    if (!(ev.getWhoClicked() instanceof Player player)){
//                        return;
//                    }
//                    if (ev.isShiftClick()) {
//                        if (player.hasPermission("oxy.quicksell") || player.isOp()){
//                            return;
//                        }
//                        return;
//                    }
//                    OxyPlugin.debug("Inv Option 1b");
//                    ev.setResult(Event.Result.DENY);
//                } else if (clickedInv.getHolder() instanceof EmptyMenu emptyMenu){
//                    ev.setCancelled(true);
//                    emptyMenu.getInventory().remove(current);
//                    bottom.addItem(current);
//                    sellMap.remove(key);
//                    OxyPlugin.debug("Inv Option 2");
//                    return;
//                }
//            }
//            OxyPlugin.log("Click");
//        }
//
//    @Override
//    public void whenClosed(InventoryCloseEvent event) {
//        if (event.getInventory().getHolder() instanceof OxyInventory oxyInventory)
//        {
//            loadInventoryToSpecialTypeMap(event);
//        }
//    }
//
//
//
//
//
//    public boolean open(){
//       return this.open(() -> inventory);
//    }
//    private boolean open(Callable<Inventory> inventoryCallable) {
//        if (Bukkit.isPrimaryThread()) {
//            OxyPlugin.debug("Cannot Open Menu on Primary Thread!");
//            return false;
//        } else {
//            Bukkit.getScheduler().runTaskAsynchronously(OxyPlugin.getPlugin(), () -> {
//                try {
//                    player.openInventory(inventoryCallable.call());
//                } catch (Exception e) {
//                    OxyPlugin.debug("Cannot Open Menu");
//                }
//            });
//
//            return player.getOpenInventory().getTopInventory().getHolder() instanceof OxyInventory oxyInventory&&oxyInventory==this;
//        }
//    }
//
//    @Override
//    public void whenRefresh(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public void whenSell(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public void whenConfirm(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public void whenViewPrice(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public void whenCancel(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public void whenHighToLow(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public void whenLowToHigh(InventoryClickEvent event, Map<Integer, GUIAnalyzedSlot> guiMap) {
//        loadInventoryToSpecialTypeMap(event);
//
//    }
//
//    @Override
//    public boolean shouldOpen(InventoryOpenEvent openEvent, Map<Integer, GUIAnalyzedSlot> stored) {
//        if (!openEvent.isCancelled()){
//            return openEvent.getPlayer().hasPermission(new ShopReference(shopRefName).getPerm());
//        }
//        return !openEvent.isCancelled();
//    }
//
//    @Override
//    public Map<Integer, GUIAnalyzedSlot> getSlots() {
//        return slotMap;
//    }
//
//
//    public void openInventory(){
//        player.openInventory(inventory);
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (!(o instanceof EmptyMenu)) return false;
//
//        EmptyMenu emptyMenu;
//        emptyMenu = (EmptyMenu) o;
//
//        return new EqualsBuilder().append(uuid, emptyMenu.uuid).isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37).append(uuid).toHashCode();
//    }
//
//
//
//}
