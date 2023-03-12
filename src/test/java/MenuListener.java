//package com.thebetterchoice.oxyman.menu;
//
//import com.thebetterchoice.oxyman.OxyPlugin;
//import com.thebetterchoice.oxyman.Stats;
//import com.thebetterchoice.oxyman.drugs.DrugRegistry;
//import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
//import com.thebetterchoice.oxyman.menu.DrugSlot;
//import com.thebetterchoice.oxyman.menu.GUIAnalyzedSlot;
//import com.thebetterchoice.oxyman.menu.NavigationSlot;
//import com.thebetterchoice.oxyman.menu.types.EmptyMenu;
//import com.thebetterchoice.oxyman.menu.types.ItemMenu;
//import com.thebetterchoice.oxyman.sales.SellList;
//import com.thebetterchoice.oxyman.utils.NavigationReference;
//import com.thebetterchoice.oxyman.utils.SellUtil;
//import io.lumine.mythic.lib.api.item.NBTItem;
//import lombok.Getter;
//import net.Indyuce.mmoitems.MMOItems;
//import net.Indyuce.mmoitems.api.Type;
//import net.Indyuce.mmoitems.gui.PluginInventory;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//import org.bukkit.event.inventory.InventoryOpenEvent;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.InventoryHolder;
//import org.bukkit.inventory.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//import java.util.Map;
//
//public class MenuListener implements Listener {
//    @Getter
//    private double currentVoucherAmount;
//    private final Map<RegisteredDrug, Integer> sellMap = DrugRegistry.getSales();
//
//
//    public MenuListener() {
//
//    }
//
//
//    @EventHandler
//    public void whenClosed(InventoryCloseEvent e) {
//        Player player = ((Player) e.getPlayer());
//        Inventory i = e.getInventory();
//        if (!(i.getHolder() instanceof OxyInventory oxyInventory)) {
//            return;
//        }
//
//        if (oxyInventory.getSlots().isEmpty()){
//            throw new RuntimeException("No Slots Loaded for " + oxyInventory.getPlayer().getDisplayName() + "'s menu.");
//        }
//
//        for (Map.Entry<Integer, GUIAnalyzedSlot> entry : oxyInventory.getSlots().entrySet()) {
//            Integer integer = entry.getKey();
//            GUIAnalyzedSlot slot = entry.getValue();
//
//            if (slot instanceof DrugSlot drugSlot){
//                RegisteredDrug registeredDrug = drugSlot.getRegisteredDrug();
//                double amount = drugSlot.getAmount();
//                double currentPrice = drugSlot.getRegisteredDrug().getCurrentPrice();
//                OxyPlugin.debug("Drug Found: " + registeredDrug.getMmoID() + " Amount: " + amount + " Current Price Found: " + currentPrice);
//                String loadd = (( drugSlot.getOxyInventory()) == oxyInventory) ? "Matching Inv" : "Invalid Inv Match";
//                OxyPlugin.explain(loadd,true);
//            }
//        }
//
//        OxyPlugin.debug("Found OxyInventory on Close");
//        if (i.getHolder() instanceof EmptyMenu emptyMenu) {
//
//            for (ItemStack itemStack : emptyMenu.getInventory()) {
//                if (itemStack == null) {
//                    continue;
//                }
//                if (!NBTItem.get(itemStack).hasType()) {
//                    continue;
//                }
//                Type type = SellUtil.getType(itemStack);
//                if (type == null) {
//                    continue;
//                }
//                if (!type.equals(Stats.drug)) {
//                    continue;
//                }
//                if (!DrugRegistry.hasDrug(MMOItems.getID(itemStack))) {
//                    continue;
//                }
//                RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(MMOItems.getID(itemStack));
//
//                if (!sellMap.containsKey(registeredDrug)) {
//                    OxyPlugin.debug("Not In Map " + MMOItems.getID(itemStack));
//                    continue;
//                }
//                Integer v = sellMap.get(registeredDrug);
//                double val = OxyPlugin.getMarket().sell(registeredDrug, Double.valueOf(v)) / v;
//                if (registeredDrug.getCurrentPrice() != val) {
//                    OxyPlugin.debug("Price a:" + registeredDrug.getCurrentPrice() + " b: " + val);
//                } else {
//                    OxyPlugin.debug("Price 1a:" + registeredDrug.getCurrentPrice() + " 2b: " + val);
//                }
//
//
//                OxyPlugin.getEconomy().depositPlayer(player, val);
//
//                OxyPlugin.debug("Amount: " + v);
//
//
//            }
//
//
//            return;
//        }
//
//
//
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onOpen(InventoryOpenEvent e) {
//        InventoryHolder holder = e.getInventory().getHolder();
//        if (holder instanceof PluginInventory mmoitemsInventory) {
//            OxyPlugin.log("Opening MMOItems Inventory");
//
//            return;
//        }
//
//        Inventory inv = e.getInventory();
//        if (!(inv.getHolder() instanceof OxyInventory oxy)) {
//            return;
//        }
//
////        if (inv.getHolder() instanceof EmptyMenu emptyMenu) {
////            emptyMenu.setSlot(0, new ItemStack(Material.MAGENTA_CONCRETE));
////            SlotHolder slotHolder = new SlotHolder(emptyMenu);
////            Map<Integer, GUIAnalyzedSlot> stored = slotHolder.getGuiMap();
////            OxyPlugin.log("Opening EmptyMenu");
////
////            if (emptyMenu.shouldOpen(e, stored)) {
////                //Open
////                OxyPlugin.debug("Opening...");
////
////                return;
////            }
////        }
//
//        try {
//            SellList.updateInv((Player) e.getPlayer(),e.getInventory(),true);
//        } catch (Exception ex) {
//            Bukkit.getConsoleSender().sendMessage("Invalid Update of SellList");
//        }
//        if (inv.getHolder() instanceof EmptyMenu emptyMenu){
//
//            ItemStack sell = MMOItems.plugin.getItem(Stats.navigation, "SELL");
//            NavigationSlot navSlot = new NavigationSlot(0, sell, new Navigation(MMOItems.getID(sell)), emptyMenu, 1);
//            emptyMenu.setSlot(navSlot);
//            Map<Integer, GUIAnalyzedSlot> stored = emptyMenu.getSlots();
//            OxyPlugin.log("Opening EmptyMenu");
//
//            boolean shouldOpen = emptyMenu.shouldOpen(e, stored);
//            if (!shouldOpen){
//                e.setCancelled(true);
//                e.getPlayer().sendMessage("Cancelling!");
//                return;
//            }
//            else {
//                e.getPlayer().sendMessage("Opening ItemMenu!");
//            }
//            int size = stored.size();
//            OxyPlugin.debug("Keys found in item list: "+ size);
//
//            return;
//        }
//        if (inv.getHolder() instanceof ItemMenu emptyMenu) {
//            emptyMenu.setSlot(0, new ItemStack(Material.MAGENTA_CONCRETE));
//            Map<Integer, GUIAnalyzedSlot> stored = emptyMenu.getSlots();
//            OxyPlugin.log("Opening EmptyMenu");
//
//            if (stored.isEmpty()){
//                emptyMenu.loadInventoryToMap(e,stored);
//            }
//
//            boolean shouldOpen = emptyMenu.shouldOpen(e, stored);
//            if (!shouldOpen){
//                e.setCancelled(true);
//                e.getPlayer().sendMessage("Cancelling!");
//                return;
//            }
//            else {
//                e.getPlayer().sendMessage("Opening ItemMenu!");
//            }
//            int size = stored.size();
//            OxyPlugin.debug("Keys found in item list: "+ size);
//        }
//
////        OxyPlugin.log("Opening Menu Page: " + oxy.getPage() + " for " + e.getPlayer().getName());
////        if (inv.getHolder() instanceof OxyInventory inventoryHolder) {
////            SlotHolder slotHolder = new SlotHolder(inventoryHolder);
////            Map<Integer, GUIAnalyzedSlot> stored = slotHolder.getGuiMap();
////            OxyPlugin.log("Opening OxyInventoryHolder");
////
////            if (inventoryHolder.shouldOpen(e, stored)) {
////
////                OxyPlugin.debug("Opening...");
////                return;
////
////            }
////        }
//
//
//        Map<Integer, GUIAnalyzedSlot> stored = oxy.getSlots();
//        OxyPlugin.log("Opening Default");
//
//        if (oxy.shouldOpen(e, stored)) {
//            OxyPlugin.debug("Opening...");
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    public void onClick(InventoryClickEvent e){
//        if (!(e.getWhoClicked() instanceof Player player)){
//            return;
//        }
//        if (!(e.getView().getTopInventory().getHolder() instanceof OxyInventory oxyInventory)){
//            return;
//        }
//
//        if (oxyInventory instanceof EmptyMenu emptyMenu){
//
//        }
//        else if (oxyInventory instanceof ItemMenu itemMenu){
//            int s = itemMenu.getInventory().getSize();
//            itemMenu.setSlot(s -1, NavigationReference.getSell());
//
//        }
//
//
//
//    }
////    @EventHandler(priority = EventPriority.HIGHEST)
////    public void whenClicked(InventoryClickEvent e) {
////        if (!(e.getWhoClicked() instanceof Player player)) {
////            return;
////        }
////        Inventory bottom = e.getView().getBottomInventory();
////        NBTItem n = NBTItem.get(e.getCurrentItem());
////        String id = n.getString("MMOITEMS_ITEM_ID");
////        Type type = getType(e.getCurrentItem());
////        Inventory clickedInv = e.getClickedInventory();
////        ItemStack current = e.getCurrentItem();
////
////        ItemStack cursor = e.getCursor();
////        if (!(clickedInv.getHolder() instanceof OxyInventory oxyInventory) && e.getView().getTopInventory() instanceof OxyInventory oxyInventory) {
////
////            if (e.isShiftClick()){
////            if (cursor != null || !NBTItem.get(cursor).hasType()) {
////                e.setResult(Event.Result.DENY);
////                return;
////            }
////            if (findSellType(MMOItems.getType(cursor)) != SellType.DRUG) {
////                //Not drug? Cancel.
////                OxyPlugin.debug("Current Found: No Drug Found!");
////                e.setResult(Event.Result.DENY);
////                return;
////            }
////
////            if (current == null || current.getType() == Material.AIR) {
////               OxyPlugin.debug("Current Found: NULL");
////                return;
////            }
////
////            if (NBTItem.get(current).hasType()) {
////
////                Type t = MMOItems.getType(current);
////                SellType sellType = findSellType(t);
////                if (sellType != SellType.DRUG) {
////                    e.setResult(Event.Result.DENY);
////                    return;
////                }
////
////            } else {
////                e.setResult(Event.Result.DENY);
////            }
////            }
////
////        } else if (e.getClickedInventory() instanceof OxyInventory oxyInventory && !(oxyInventory instanceof EmptyMenu emptyMenu) && !(oxyInventory instanceof OxyInventoryHolder)) {
////            bottom = e.getView().getBottomInventory();
////            n = NBTItem.get(current);
////            id = n.getString("MMOITEMS_ITEM_ID");
////            type = getType(current);
////            clickedInv = e.getClickedInventory();
////            if (clickedInv == null) {
////                return;
////            }
////            if (bottom == clickedInv) {
////                e.setResult(Event.Result.DENY);
////                OxyPlugin.debug("Second Chance of Failure");
////                return;
////            }
////        }else if (e.getClickedInventory() instanceof OxyInventoryHolder oxyInventoryHolder){
////            whenClicked(e);
////            OxyPlugin.debug("Clicked Inventory = OxyInventoryHolder");
////
////        }
////        else if (e.getClickedInventory() instanceof EmptyMenu emptyMenu){
////            if (e.getCursor()!=null||e.getCursor().getType()!=Material.AIR&& !NBTItem.get(e.getCursor()).hasType()){
////                if (findSellType(Type.get(e.getCursor())) != SellType.DRUG){
////                    e.setResult(Event.Result.DENY);
////                }
////                return;
////            }
////            boolean changed = false;
////            if (e.getCurrentItem() != null && !NBTItem.get(e.getCurrentItem()).hasType()){
////                if (e.getCursor()!=null&&NBTItem.get(e.getCursor()).hasType()){
////
////                    if (findSellType(MMOItems.getType(e.getCursor()))== SellType.DRUG){
////                        e.setCurrentItem(e.getCursor().clone());
////                        changed = true;
////                    }
////
////                }
////                e.getView().getBottomInventory().addItem(e.getCurrentItem().clone());
////                if (!changed){
////                    e.setCurrentItem(null);
////                }
////                e.setCancelled(true);
////                return;
////            }
////            emptyMenu.whenClicked(e);
////            OxyPlugin.debug("Clicked Inventory = EmptyMenu");
////        }
////
////    }
////            if (e.getClickedInventory().getHolder() instanceof OxyInventoryHolder inventoryHolder) {
////                NavigationSlot[] loc = loadNavigation(menuSlots, inventoryHolder);
////                Navigation navigation = null;
////                List<NavigationSlot> locations = Arrays.stream(loc).toList();
////                List<NavigationSlot> exact = new ArrayList<>(locations);
////
////                if (cursor == null) {
////                    if (menuSlots.containsValue(current)) {
////                        type = getType(current);
////                        sellType = findSellType(type);
////
////                        if (sellType == SellType.VANILLA || type == null) {
////                            return;
////                        }
////
////                        if (isNavigation(sellType)) {
////                            id = NBTItem.get(current).getString("MMOITEMS_ITEM_ID");
////                            navigation = new Navigation(id);
////                            Navigation finalNavigation = navigation;
////                            if (exact.isEmpty()) {
////                                OxyPlugin.log("No Navigation Exact Found");
////                                return;
////                            }
////                            exact.removeIf(navigationSlot -> !navigationSlot.getNavigation().equals(finalNavigation));
////
////                            if (debugLists(locations, exact)) return;
////
////                            NavigationSlot navigationSlot = exact.get(0);
////                            Navigation.KeyType navigationSent = navigationSlot.getNavigation().onNavigate(new OxyInventoryHolder(player), e);
////
////                            OxyPlugin.debug("Executing: " + navigationSent.name().toUpperCase(Locale.ROOT) + " with " + e.getWhoClicked().getName());
////                            return;
////                        }
////
////                        if (current == null) {
////                            return;
////                        }
////                        if (canSell(sellType, current)) {
////
////                            double multiplier = 1.0D;
////                            double tax;
////
////                            for (PermissionInstance permissionInstance : PermissionLoader.getPermissionsUsed()) {
////                                if (!player.hasPermission(permissionInstance.getFullPerm())) {
////                                    continue;
////                                }
////                                if (!player.isPermissionSet(permissionInstance.getFullPerm())) {
////                                    continue;
////                                }
////                                multiplier = multiplier + permissionInstance.getValue();
////                            }
////
////                            multiplier += OxyPlugin.getGlobalBonus().getCurrentBonus();
////
////                            if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())) {
////                                multiplier += (OxyPlugin.playerBoosts.get(player.getUniqueId()).getCurrentBonus());
////                            }
////
////
////                            sell(player,current, current.getAmount(), multiplier);
////                            return;
////                        }
////
////                    }
////                    return;
////                }
////
////                ItemStack cursorItem = cursor;
////                VoucherItem.BonusType b = findBonusType(cursorItem);
////                VoucherItem voucherItem;
////                if (e.isLeftClick()) {
////                    e.setCancelled(true);
////                    return;
////                }
////                if (b == VoucherItem.BonusType.NONE) {
////                    OxyPlugin.log("No Voucher Bonus Found");
////                    return;
////                }
////                if (e.isShiftClick()) {
////                    voucherItem = new VoucherItem(b, cursorItem);
////
////                    String textToSend = MythicLib.plugin.parseColors("&aVoucher: &b" + voucherItem.getName() + "&a Value: &b" + voucherItem.getActualValue() + "&a Type: &b" + voucherItem.getBonusType() + "&a Length: &b" + voucherItem.getLength());
////                    ((Player) e.getWhoClicked()).sendRawMessage(textToSend);
////                    e.setCancelled(true);
////                    return;
////                }
////                if (!e.isRightClick()) {
////                    ((Player) e.getWhoClicked()).sendRawMessage(MythicLib.plugin.parseColors("&ePlease right click to activate bonus or Shift-Right to view details!"));
////                }
////                double value = 0;
////                value = b.getValuePercent();
////
////                Bonus bonus = new Bonus(value, b.getLength());
////                PlayerBonusHandler bonusHandler = OxyPlugin.playerBoosts.get(e.getWhoClicked().getUniqueId());
////                bonusHandler.addBonus(bonus);
////            }
//
//
//    private boolean debugLists(List<NavigationSlot> locations, @NotNull List<NavigationSlot> exact) {
//        if (exact.isEmpty()) {
//            OxyPlugin.log("No Found MenuKeys in Parsed List");
//            return true;
//        } else {
//            OxyPlugin.log("Located NavigationKeys: " + exact.size() + "/" + locations.size());
//        }
//        return false;
//    }
//
////    @NotNull
////    private NavigationSlot[] loadNavigation(Map<Integer, ItemStack> menuSlots, OxyInventory oxyInventory) {
////        NavigationSlot[] loc = ArrayUtils.createArray(NavigationSlot.class, 9);
////        int tracking = 0;
////        for (Map.Entry<Integer, ItemStack> entry : menuSlots.entrySet()) {
////            Integer integer = entry.getKey();
////            ItemStack itemStack = entry.getValue();
////            Type t = getType(itemStack);
////            if (t != null && t == Stats.navigation) {
////
////                Navigation guiItem = new Navigation(MMOItems.getID(itemStack),new NavigationSlot());
////                NavigationSlot m = new NavigationSlot(integer, itemStack, guiItem, oxyInventory,itemStack.getAmount());
////                io.lumine.mythic.bukkit.utils.lib.lang3.ArrayUtils.add(loc, tracking, m);
////                ++tracking;
////            }
////        }
////        if (io.lumine.mythic.bukkit.utils.lib.lang3.ArrayUtils.isEmpty(loc)) {
////            throw new RuntimeException("NO Navigation Found");
////        }
////        return loc;
////    }
//
////
////    private void sell(Player player,ItemStack item, double amountSelling, double multiplierAmount) {
////        String id = MMOItems.getID(item);
////        double sellAmount = 0;
////        if (DrugRegistry.hasDrug(MMOItems.getID(item))) {
////            RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(MMOItems.getID(item));
////            double val = amountSelling * registeredDrug.getCurrentPrice();
////
////            sellAmount = multiplierAmount > 0 ? multiplierAmount * val : val;
////
////            if (sellAmount>0) {
////                double sell = OxyPlugin.getMarket().sell(registeredDrug, amountSelling);
////                OxyPlugin.getEconomy().depositPlayer(player, sellAmount);
////            }}
////    }
////    public Type getType(ItemStack itemStack){
////        NBTItem nbtItem = NBTItem.get(itemStack);
////        return nbtItem.hasType()? MMOItems.getType(nbtItem):null;
////    }
////    public Type getTypeOrThrow(ItemStack itemStack) {
////        if (getType(itemStack) == null) {
////            throw new RuntimeException("No Type Found for Item: " + itemStack.getItemMeta().getDisplayName());
////        }
////        return getType(itemStack);
////    }
////
////        private SellUtil.SellType findSellType (Type type){
////            SellUtil.SellType sellType = SellUtil.SellType.NONE;
////            if (type == null) {
////                sellType = SellUtil.SellType.VANILLA;
////            } else if (type.corresponds(Stats.drug) || type == Stats.drug) {
////                sellType = SellUtil.SellType.DRUG;
////            } else if (type.corresponds(Stats.navigation) || type == Stats.navigation) {
////                sellType = SellUtil.SellType.NAVIGATION;
////            } else {
////                sellType = SellUtil.SellType.UNKNOWN;
////            }
////
////            return sellType;
////        }
////        public boolean isNavigation (SellUtil.SellType sellType){
////            return sellType == SellUtil.SellType.NAVIGATION;
////        }
//
//    }