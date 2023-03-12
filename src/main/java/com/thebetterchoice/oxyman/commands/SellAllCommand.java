package com.thebetterchoice.oxyman.commands;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.events.DrugSellEvent;
import com.thebetterchoice.oxyman.sales.GlobalBonusHandler;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import com.thebetterchoice.oxyman.utils.MessageConstants;
import com.thebetterchoice.oxyman.utils.SmartSell;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SellAllCommand implements CommandExecutor {
    private final Map<UUID, Long> confirm = new HashMap<>();
    private double multiplier;
    private double noEventPrice;
    private double price;
    private boolean drugFound = false;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)){
        sender.sendMessage("&cYou must be a player to sell all!");
        return false;
    }
        var var6 = this.confirm.keySet().iterator();


    UUID uuid;
    while(var6.hasNext()) {
        uuid = var6.next();
        long value = this.confirm.get(uuid);
        if (System.currentTimeMillis() - value >= 60000L) {
            this.confirm.remove(uuid);
        }
    }


    RegisteredDrug registeredDrug = null;

    if (args.length == 1) {
        try {
            String id = args[0];
            MMOItem drug = MMOItems.plugin.getMMOItem(Stats.DRUG, id);
            if (drug == null){
                OxyPlugin.log("No Drug Found: " + id);
                return false;
            }
            if (!DrugRegistry.getRegisteredDrugs().containsKey(drug.getId())) {
                sender.sendMessage(MessageConstants.get().getSELL_ITEM_NOT_FOUND() + "!");
                return false;
            }

            registeredDrug = DrugRegistry.getRegisteredDrugs().get(drug.getId());

            if (registeredDrug == null) {
                OxyPlugin.log("Drug Register Not Found: " + id + " in SellAll command.");
                return false;
            }
            double totalVanilla = 0;
            boolean sellVanilla = false;
            for (ItemStack item : player.getInventory()) {
                if (!NBTItem.get(item).hasType()){
                    Material material = Material.getMaterial(id);
                    if (material == null){
                        continue;
                    }
                    if (!DrugRegistry.getVanillaPrices().containsKey(material)){
                        continue;
                    }
                    if (item.getType() != material) {
                        continue;
                    }
                    sellVanilla = true;
                    Double price = DrugRegistry.getVanillaPrices().get(material);
                    totalVanilla += price;
                    player.getInventory().remove(material);
                }
            }
            if (sellVanilla) {
                OxyPlugin.getEconomy().depositPlayer(player, totalVanilla);
                player.sendRawMessage(MythicLib.plugin.parseColors(MessageConstants.get().getSELL().replace("%value%",totalVanilla + "")));
            }

        }
        catch (Exception base){
             return false;
        }




            if (noEventPrice < 0.0D) {
                noEventPrice = Math.abs(noEventPrice) / 100.0D;
            } else {
                noEventPrice /= 100.0D;
            }
            multiplier += noEventPrice;


        var permissionTotal = OxyPlugin.getPermissionLoader().getTotalPermissions(player);


        if (permissionTotal>0 && permissionTotal <= 1){
            multiplier += permissionTotal;
        }

        GlobalBonusHandler globalBonus = OxyPlugin.getGlobalBonus();
        if (globalBonus.getCurrentBonus() != 0){
            multiplier += globalBonus.getCurrentBonus();
        }
        OxyUser user = OxyUser.getStored(player);
        if (user == null){
            throw new RuntimeException("Invalid Oxy User Player for Sell Event");

        }


    multiplier += OxyPlugin.getGlobalBonus().getCurrentBonus();
    if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())) {
        multiplier += OxyPlugin.playerBoosts.get(player.getUniqueId()).getCurrentBonus();
    }

    price = 0.0D;
    noEventPrice = 0.0D;
    Map<RegisteredDrug,Integer> drugs = new HashMap<>();
        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack == null){
                continue;
            }
            if (!NBTItem.get(itemStack).hasType()){
                continue;
            }
            if (MMOItems.getType(NBTItem.get(itemStack))!=(Stats.DRUG) && !MMOItems.getType(NBTItem.get(itemStack)).getId().equalsIgnoreCase(Stats.DRUG.getId()) ) {
                continue;
            }
            MMOItem drug = MMOItems.plugin.getMMOItem(Stats.DRUG,MMOItems.getID(NBTItem.get(itemStack)));
            if (drug == null){
                continue;
            }
            registeredDrug = new RegisteredDrug(drug.getId());
            drugFound = true;
            price += OxyPlugin.getMarket().sell(registeredDrug, (double) itemStack.getAmount());

            if (drugs.containsKey(registeredDrug)){
                drugs.replace(registeredDrug,drugs.get(registeredDrug) + itemStack.getAmount());
            }
            else {
                drugs.put(registeredDrug, itemStack.getAmount());
            }
            noEventPrice += OxyPlugin.getMarket().sellIgnoreEvent(registeredDrug,itemStack.getAmount());
        }

    if (drugFound) {
        if (price != noEventPrice && price <= noEventPrice && !this.confirm.containsKey(player.getUniqueId())) {
            //player.sendMessage(pre + ChatColor.GRAY + " If you presently sell, you would lose " + ChatColor.RED + "Â£" + (new DecimalFormat("#,###.00")).format((noEventPrice - price) * multiplier) + ChatColor.GRAY + " due to an event. Do " + ChatColor.RED + "/sellall " + ChatColor.GRAY + "to confirm.");
            player.sendRawMessage(MythicLib.plugin.parseColors(MessageConstants.get().getSELL_LOW_PRICE_WARNING().replace("%value%", MythicLib.plugin.getMMOConfig().newDecimalFormat("#,###.00").format((noEventPrice - price) * multiplier).replace("%currency%", MessageConstants.get().getCURRENCY()))));
        } else {

            //Discard System - Use this for MMOItems
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack == null) {
                    continue;
                }
                NBTItem m = NBTItem.get(itemStack);
                if (!m.hasType()) {
                    if (OxyPlugin.getFileConfiguration().contains("Discard")) {
                        List<String> discord = OxyPlugin.getFileConfiguration().getStringList("Discard");
                        for (String key : discord) {
                            if (key.startsWith("!")){
                                ItemStack i = MMOItems.plugin.getItem(MMOItems.plugin.getTypes().getOrThrow(m.getType()), MMOItems.getID(m));
                                if (i == null){
                                    OxyPlugin.log("Discard Item: " + key + " not found.");
                                    continue;
                                }
                                player.getInventory().remove(i);
                            }
                            if (itemStack.getType().getKey().getKey().equalsIgnoreCase(key)) {
                                player.getInventory().remove(itemStack);
                            }
                        }
                    }
                    continue;
                }
                if (ConfigReference.garbage.contains(itemStack.getType().getKey().getKey())) {
                    OxyPlugin.debug("Item Marked for Garbage: " + itemStack.getType());
                    player.getInventory().remove(itemStack);
                }

            }

        double total = price * multiplier;


        int amount;
        double totalValue = 0;
        for (Map.Entry<RegisteredDrug, Integer> entry : drugs.entrySet()) {
            RegisteredDrug registered = entry.getKey();
            amount = entry.getValue();
            if (player.hasPermission("oxy.ignore")) {
                continue;
            }
            MMOItem i = registered.getItem();
            DrugSellEvent sellEvent = new DrugSellEvent(registered, amount, OxyUser.getStored(player));
            Bukkit.getPluginManager().callEvent(sellEvent);

            double value = 0;
            if (!sellEvent.isCancelled()) {
                HashMap<Integer, ItemStack> sell = new HashMap<>();
                new SmartSell(player, sell).giveDrops();

                //Sell Here
                OxyPlugin.debug("Sent Event, Sold Items?");
            }
            else {
                OxyPlugin.debug("Sell Event Not Happening or Cancelled?");
            }
            totalValue = (value * multiplier) + totalValue;
        }
        if (total!=totalValue){
            OxyPlugin.log("Invalid Total for Sell All Command: " + total + " : " + totalValue);
        }
        return total==totalValue;

        }
    }

    OxyPlugin.log("No Sell Happened");
        return false;
    }
    return false;
    }
}

//            for(Iterator<RegisteredDrug> var20 = drugs.keySet().iterator(); var20.hasNext(); Bukkit.getServer().getPluginManager().callEvent(new DrugSellEvent(r, amountOfItems, player))) {
//                amountOfItems = drugs.get(id);
//                if (!player.hasPermission("oxydealer.ignore")) {
//                    Main.market.influence(id, amountOfItems);
//                }
//            }


