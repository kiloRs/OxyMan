//package com.thebetterchoice.oxyman.menu;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.Stats;
//import com.thebetterchoice.oxyman.drugs.DrugRegistry;
//import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
//import com.thebetterchoice.oxyman.menu.GUIAnalyzedSlot;
//import de.jeff_media.jefflib.EnumUtils;
//import io.lumine.mythic.bukkit.utils.lib.lang3.Validate;
//import io.lumine.mythic.lib.api.item.NBTItem;
//import lombok.Getter;
//import net.Indyuce.mmoitems.MMOItems;
//import net.Indyuce.mmoitems.api.Type;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.Locale;
//import java.util.Map;
//
//public class Navigation {
//    @Getter
//    private final Type type;
//    @Getter
//    private final KeyType keyType;
//
//    @Getter
//    private final String id;
//
//    public Navigation(NBTItem nbtItem){
//        this(nbtItem.hasType()?nbtItem.getString("MMOITEMS_ITEM_ID"):null);
//    }
//    public Navigation(String id){
//        Validate.notNull(id,"Invalid Nav Gear");
//        type = Stats.navigation;
//        this.id = id;
//        keyType = EnumUtils.getIfPresent(KeyType.class, id).orElse(KeyType.OTHER);
//
//        if (keyType == KeyType.OTHER){
//            OxyPlugin.log("Bad Navigation Key Found for " + id);
//            return;
//        }
//        OxyPlugin.log("Loading Navigation: " + keyType.name().toUpperCase(Locale.ROOT));
//    }
//
//    /**
//     * @param e Event of clicking an item onto the sell button, will sell it automatically.
//     * @param sellable The item to sell.
//     * @param deposit If it should force sell, or require perms if false.
//     * @return selling, if sellable is right type, and if drug registry holds it.
//     */
//    public boolean sellOnSell(InventoryClickEvent e, ItemStack sellable, boolean deposit){
//        String id = NBTItem.get(sellable).getString("MMOITEMS_ITEM_ID");
//
//        if (!NBTItem.get(sellable).hasType()){
//            return false;
//        }
//        if (!id.equalsIgnoreCase(MMOItems.getID(e.getCursor()))){
//            return false;
//        }
//
//        if (!DrugRegistry.hasDrug(id)) {
//            return false;
//        }
//        RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(id);
//
//
//        if (keyType == KeyType.SELL) {
//            Player whoClicked = (Player) e.getWhoClicked();
//            double value = OxyPlugin.getMarket().sell(registeredDrug, (double) sellable.getAmount());
//            if (whoClicked.hasPermission("oxy.navigation.sell.individual") || deposit) {
//                //Permission Based Selling or Override
//                OxyPlugin.getEconomy().depositPlayer(whoClicked,value);
//                e.setCursor(null);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Sends each navigation to its own inventoryHolders control switch for the navigation.
//     * @param inventoryHolder Custom Inventory
//     * @param event Event to navigate from...
//     * @return
//     */
//    public KeyType onNavigate(OxyInventory inventoryHolder, InventoryClickEvent event){
//        Map<Integer, GUIAnalyzedSlot> guiMap = inventoryHolder.getSlots()
//
//        switch (keyType){
//
//            case REFRESH, FILLER -> {
//                inventoryHolder.whenRefresh(event, guiMap);
//            }
//            case SELL -> {
//                inventoryHolder.whenSell(event,guiMap);
//            }
//            case CONFIRM -> {
//                inventoryHolder.whenConfirm(event,guiMap) ;
//            }
//            case PRICE -> {
//                inventoryHolder.whenViewPrice(event,guiMap);
//            }
//            case CANCEL -> {
//                inventoryHolder.whenCancel(event,guiMap);
//            }
//            case SKULL, OTHER -> {
//                OxyPlugin.log("Invalid Click?");
//            }
//            case HIGHtoLOW -> {
//                inventoryHolder.whenHighToLow(event,guiMap);
//            }
//            case LOWtoHIGH -> {
//                inventoryHolder.whenLowToHigh(event,guiMap);
//            }
//        }
//        return keyType;
//    }
//
//
//
//    public enum KeyType{
//        REFRESH,SELL,CONFIRM,PRICE,FILLER,CANCEL,SKULL,LOWtoHIGH,HIGHtoLOW,OTHER
//    }
//
//}
