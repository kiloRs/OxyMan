package com.thebetterchoice.oxyman.commands;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.utils.NavigationReference;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SellCommand implements CommandExecutor {
    private final ItemStack filler;
    private final ItemStack cancel;
    private final ItemStack sell;
    private final ItemStack confirm;
    private final ItemStack price;
    private final ItemStack skull;
    private final List<Player> playersInConfirmation = new ArrayList<>();
    private double totalPrice = 0;
    private int amountSelling = 0;
    private ItemStack itemSelling = null;
    private String argument = "";

    public SellCommand() {
        this.filler = NavigationReference.getFiller();
        this.cancel = NavigationReference.getCancel();
        this.confirm = NavigationReference.getConfirm();
        this.sell = NavigationReference.getSell();
        this.price = NavigationReference.getPrice();
        this.skull = NavigationReference.getSkull();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("oxysell")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("This command can only be run by a player!");
                return true;
            }
            Inventory inventory = player.getInventory();
            argument = args[0];
            for (ItemStack item : inventory.getContents()) {
                if (item == null) {
                    continue;
                }
                if (!NBTItem.get(item).hasType()) {
                    continue;
                }
                if (!NBTItem.get(item).getType().equalsIgnoreCase(Stats.DRUG.getId())) {
                    continue;
                }
                if (!MMOItems.getID(NBTItem.get(item)).equalsIgnoreCase(argument)) {
                    continue;
                }
                RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(argument);
                if (registeredDrug.getCurrentCost() == 0) {
                    continue;
                }
                double itemPrice = item.getAmount() * registeredDrug.getCurrentCost();
                totalPrice += itemPrice;

                if (itemPrice > 0) {
                    itemSelling = item;
                    amountSelling = item.getAmount();
                }
            }
            if (totalPrice > 0.0) {
                playersInConfirmation.add(player);
                player.sendMessage("You are about to sell all " + argument + " items in your inventory for a total price of " + totalPrice + " $. Type '/oxyconfirm' to confirm, or anything else to cancel.");
            } else {
                player.sendMessage("You don't have any " + argument + " items in your inventory.");
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("oxyconfirm") && sender instanceof Player player) {
            if (playersInConfirmation.contains(player)) {

                if (itemSelling != null && amountSelling>0) {
                    player.getInventory().remove(itemSelling.asQuantity(amountSelling));
                    OxyPlugin.getEconomy().depositPlayer(player, totalPrice);
                    player.sendMessage("You sold all " + argument + " items in your inventory for a total price of " + totalPrice + " diamonds!");
                    totalPrice = 0;
                    itemSelling = null;
                    amountSelling = 0;
                }
                playersInConfirmation.remove(player);
                return true;
            }
            return true;
        }
        return false;
    }
}
//    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        this.filler = NavigationReference.getFiller();
//        this.cancel = NavigationReference.getCancel();
//        this.confirm = NavigationReference.getConfirm();
//        this.sell = NavigationReference.getSell();
//        this.price = NavigationReference.getPrice();
//        this.skull = NavigationReference.getSkull();
//        if (!(sender instanceof Player player)) {
//            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
//            return true;
//        } else {
//            MenuInstance menuInstance = null;
//            if (args[0].equalsIgnoreCase("list") && args[1].equalsIgnoreCase("*")){
//                menuInstance = new MenuInstance(player, true);
//            }
//            else if (args[0].equalsIgnoreCase("list") && args[1].isEmpty() || args.length == 1 && args[0].equalsIgnoreCase("list")){
//                menuInstance = new MenuInstance(player,false);
//            }
//            else {
//                player.sendRawMessage("Invalid List! ");
//                return false;
//            }
//            if (DrugRegistry.getRegisteredDrugs().size() == 0) {
//                throw new RuntimeException("No Drugs Loaded!");
//            }
//            if (menuInstance == null){
//                throw new RuntimeException("No Menu Instance!");
//            }
//            String arg = args[1];
//            Inventory sellList;
//
//            sellList = menuInstance.getInventory();
//
//            int multiplier = 0;
////            if (OxyPlugin.getFileConfiguration().isConfigurationSection("multipliers")){
////                var var8 = OxyPlugin.getFileConfiguration().getConfigurationSection("multipliers").getKeys(false).iterator();
////
////                String multiDisplay;
////                while(var8.hasNext()) {
////                    multiDisplay = var8.next();
////                    if (player.hasPermission("oxydealer.multiplier." + multiDisplay)) {
////                        multiplier = multiplier + OxyPlugin.getFileConfiguration().getInt("multipliers." + multiDisplay);
////                    }
////                }
////            }
//
//
//            var permissionTotal = OxyPlugin.getPermissionLoader().getTotalPermissions(player);
//
//
//            if (permissionTotal > 0) {
//                multiplier += permissionTotal;
//            }
//
//            multiplier += (int) (OxyPlugin.getGlobalBonus().getCurrentBonus() * 100.0D);
//            if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())) {
//                multiplier += (int) (OxyPlugin.playerBoosts.get(player.getUniqueId()).getCurrentBonus() * 100.0D);
//            }
//
//            if (menuInstance != null){
//                SellList.updateInv(player,sellList,true);
//                player.openInventory(sellList);
//                OxyPlugin.log("Opening Menu of Testing Values...");
//                return true;
//            }
//            if (label.equalsIgnoreCase("selllist") || args.length == 2 && args[0].equalsIgnoreCase("list")) {
//                SellList.updateInv(player, sellList, true);
//                player.openInventory(sellList);
//                OxyPlugin.getInventories().put(sellList, true);
//                return true;
//            } else {
//                String multiDisplay = "";
//                int temp = multiplier;
//                if (OxyPlugin.getMarket().getCurrentEvent() != null && OxyPlugin.getMarket().getCurrentEvent().getModifier() != 0.0D && OxyPlugin.getMarket().getCurrentEvent().getAffects().isEmpty()) {
//                    temp = multiplier + (int) OxyPlugin.getMarket().getCurrentEvent().getModifier();
//                }
//
//                if (temp >= 0) {
//                    multiDisplay = " " + ChatColor.DARK_GRAY + "(" + ChatColor.DARK_GREEN + "+" + temp + "%" + ChatColor.DARK_GRAY + ")";
//                } else {
//                    multiDisplay = " " + ChatColor.DARK_GRAY + "(" + ChatColor.RED + temp + "%" + ChatColor.DARK_GRAY + ")";
//                }
//
//                if (arg.equalsIgnoreCase("item") && player.getInventory().getItemInMainHand().getType()== Material.CACTUS) {
//                    //Inventory gui = Bukkit.createInventory(OxyPlugin.getMarket(), Market.sizeOfMenu,"Market " + multiDisplay);
//                    sellList.setItem(sellList.getSize() - 5, this.price);
//                    sellList.setItem(sellList.getSize() - 4, this.sell);
//                    MMOItem builder = MMOItems.plugin.getMMOItem(Stats.NAVIGATION, MMOItems.getID(skull));
//                    if (OxyPlugin.getMarket().getCurrentEvent() != null) {
//                        ArrayList<String> lore = new ArrayList<>(Arrays.asList(OxyPlugin.getMarket().getCurrentEvent().getPath().split("\\|")));
//                        double mod = OxyPlugin.getMarket().getCurrentEvent().getModifier();
//                        if (mod > 0.0D) {
//                            lore.add("Drug Sale Price: " + ChatColor.GREEN + ChatColor.BOLD + "+" + OxyPlugin.getMarket().getCurrentEvent().getModifier() + "%");
//                        } else {
//                            lore.add("Drug Sale Price: " + ChatColor.RED + ChatColor.BOLD + OxyPlugin.getMarket().getCurrentEvent().getModifier() + "%");
//                        }
//                        if (builder == null) {
//                            throw new RuntimeException("Invalid News Item");
//                        }
//                        builder.setData(Stats.news, new StringListData(lore));
//                        //todo In theory, this should apply the news lore to theis item in a special stat, which is then accessed externally to view through mmoitems lore.
//                        OxyPlugin.debug("Multi-display: " + multiDisplay);
//                        StringListData list = (StringListData) builder.getData(Stats.news);
//
//                        if (list.getList().isEmpty()) {
//                            throw new RuntimeException("Empty Lore List for News");
//                        }
//                        ItemStackBuilder is = builder.newBuilder();
//                        sellList.setItem(sellList.getSize() - 2, is.buildSilently());
//                        OxyPlugin.debug("Displaying News");
//                    }
////                        RegisteredDrug registeredDrug;
////                        ItemStack mainHand = player.getInventory().getItemInMainHand();
////                        if (NBTItem.get(mainHand).hasType()) {
////                            Type type = MMOItems.getType(mainHand);
////                            if (type == null){
////                                OxyPlugin.throwError("Bad Type!");
////                                return  false;
////                            }
////                            if (type.corresponds(Stats.drug)){
////                                String mmoitems_item_id = NBTItem.get(mainHand).getString("MMOITEMS_ITEM_ID");
////                                MMOItem mmoItem = MMOItems.plugin.getMMOItem(Stats.drug, mmoitems_item_id);
////                                registeredDrug = DrugRegistry.hasDrug(mmoitems_item_id)?DrugRegistry.getRegisteredDrugs().get(mmoitems_item_id):null;
////                            }
////                        }
////                                double price = 0;
////                                int amount;
////                                for (ItemStack itemStack : t) {
////                                    if (NBTItem.get(itemStack).hasType() && NBTItem.get(itemStack).getType().equalsIgnoreCase(Stats.navigation.getId())){
////                                        continue;
////                                    }
////
////                                    if (!NBTItem.get(itemStack).hasType() || !NBTItem.get(itemStack).getType().equalsIgnoreCase(Stats.drug.getId())) {
////                                        continue;
////                                    }
////                                    String id = NBTItem.get(itemStack).getString("MMOITEMS_ITEM_ID");
////                                    registeredDrug =  DrugRegistry.hasDrug(id)?DrugRegistry.getRegisteredDrugs().get(id):null;
////                                    amount = itemStack.getAmount();
////                                    if (registeredDrug == null){
////                                        OxyPlugin.log("Not Registered Drug: " + id);
////                                        continue;
////                                    }
////                                    double amountFinalSell = OxyPlugin.getMarket().sell(registeredDrug, (double) amount);
////
////                                    OxyPlugin.log("Current Total: "+ price);
////                                    OxyPlugin.log("Selling Item Price: " + amountFinalSell + " for " + amount + " of " + registeredDrug.getPath() + "'s");
////                                    price = (price + amountFinalSell);
////                                    OxyPlugin.log("New Total: " + price);
////                                }
////                                if (price>=0){
////                                    OxyPlugin.log("Price without modifiers: " + price);
////                                }
//
//                    }
//                }
//                OxyPlugin.log("Opening Sell List");
//
//                return true;
//
//            }
////                    if (OxyPlugin.getMarket().getCurrentEvent() != null) {
////                        headMeta.setDisplayName(ChatColor.DARK_RED + "BREAKING NEWS");
//
////                        ArrayList<String> lore = new ArrayList<>(Arrays.asList(OxyPlugin.getMarket().getCurrentEvent().getName().split("\\|")));
////                        double mod = OxyPlugin.getMarket().getCurrentEvent().getModifier();
////                        if (mod > 0.0D) {
////                            lore.add( "LoadingDrug Sale Price: " + ChatColor.GREEN + ChatColor.BOLD + "+" + OxyPlugin.getMarket().getCurrentEvent().getModifier() + "%");
////                        } else {
////                            lore.add( "LoadingDrug Sale Price: " + ChatColor.RED + ChatColor.BOLD + OxyPlugin.getMarket().getCurrentEvent().getModifier() + "%");
////                        }
////                        headMeta.setLore(lore);
////                    } else {
////                        headMeta.setDisplayName(ChatColor.DARK_RED + "24/7 NEWS");
////                        headMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "There's currently nothing", ChatColor.DARK_GRAY + "to report in Oxytropolis!"));
////                    }
////---------------------------------------------------------------------------------------------------
////                    gui.setItem(gui.getSize() - 1, skull);
////                    gui.setItem(gui.getSize() - 2, this.cancel);
////                    ShopInventory market = new ShopInventory(player, "Market", multiDisplay);
////                    market.open(market.getInventory());
////                    OxyPlugin.getInventories().put(gui, false);
//        }
//    }

