package com.thebetterchoice.oxyman.commands;

import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.BasicDrug;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import io.lumine.mythic.lib.MythicLib;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CheckItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
        if (args[0].equalsIgnoreCase("test")){
            if (sender instanceof Player player) {

                //Finish This

                return false;
            }
        }
       if (args[0].equalsIgnoreCase("!shop")){
           if (sender instanceof Player player){
               if (player.getInventory().getItemInMainHand().getType()== Material.CACTUS){
                   //Finish This
               return true;
           }
       }

            if (args[0].isEmpty()){
                sender.sendMessage(MythicLib.plugin.parseColors("&cNo Item Entered as Text!"));
                return false;
            }
            String checked = args[0];
            if (checked.equalsIgnoreCase(".")){
                Player player = (Player) sender;
                RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(MMOItems.getID(player.getInventory().getItemInMainHand()));

                double knownPrice = registeredDrug.getCurrentCost();
                double availPrice = new BasicDrug(player.getInventory().getItemInMainHand()).getPrice();
                if (knownPrice > -1){
                    sender.sendMessage("Known Price: " + knownPrice + " Available Price: " + availPrice );
                    return true;
                }
            }
            MMOItem mmoItem = MMOItems.plugin.getMMOItem(Stats.DRUG, checked);

            if (mmoItem == null || !DrugRegistry.hasDrug(mmoItem)){
                sender.sendMessage("Item Not Found");
                return true;
            }

            RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(mmoItem.getId());

           double knownPrice = -1;
           double availPrice = 0;

           if (sender instanceof Player player) {
               knownPrice = registeredDrug.getDrug(((Player) sender).getInventory().getItemInMainHand()).getPrice();
               availPrice = new BasicDrug(player.getInventory().getItemInMainHand()).getPrice();

           }

           knownPrice = registeredDrug.getCurrentCost();
           if (knownPrice > -1){
               sender.sendMessage("Known Price: " + knownPrice + " Available Price: " + availPrice );
               return true;
           }
        }
       sender.sendMessage("Not Found!");
        return false;
    }
        return false;
    }
}
