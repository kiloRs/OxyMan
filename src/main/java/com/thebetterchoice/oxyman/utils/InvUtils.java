package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.PermissionInstance;
import com.thebetterchoice.oxyman.PermissionLoader;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.sales.PlayerBonusHandler;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InvUtils {

    public boolean sell(InventoryClickEvent e, boolean deposit) {
        if (e.getCurrentItem() == null) {
            return false;
        }


        this.sell(((Player) e.getWhoClicked()), e.getCurrentItem(), e.getCurrentItem().getAmount(), deposit);
        return false;
    }

    public boolean sell(Player player, ItemStack itemStack, boolean deposit){
       return this.sell(player,itemStack,itemStack.getAmount(),deposit);
    }
    public boolean sell(Player player, ItemStack itemStack, int amountSold, boolean deposit){
        double global = OxyPlugin.getGlobalBonus().getCurrentBonus();
        double personal = 0;
        if (OxyPlugin.playerBoosts.containsKey(player.getUniqueId())) {
            PlayerBonusHandler playerBonusHandler = OxyPlugin.playerBoosts.get(player.getUniqueId());
            personal = playerBonusHandler.getCurrentBonus();
        }
        double totalMultipliers = personal + global;

        double permValue = 0;
        for (PermissionInstance instance : PermissionLoader.getPermissionsUsed()) {
            OxyUser user = OxyUser.getStored(player);
//            if (user.getActivePermissions().containsKey(instance)) {
//                permValue += user.getActivePermissions().get(instance);
//            }
            if (player.hasPermission(instance.getFullPerm())){
                permValue = instance.getValue() + permValue;
            }
        }
        double finalMultiplier = totalMultipliers + permValue;

       return this.sell(player,itemStack,amountSold,finalMultiplier,deposit);

    }
    private boolean sell(Player player, ItemStack item, double amountSelling, double multiplierAmount, boolean intoDeposit) {
        String id = MMOItems.getID(item);
        double sellAmount = 0;
        if (DrugRegistry.hasDrug(id)) {
            RegisteredDrug registeredDrug = DrugRegistry.getRegisteredDrugs().get(MMOItems.getID(item));
            double val = amountSelling * registeredDrug.getCurrentCost();
            double sellMarket = OxyPlugin.getMarket().sell(registeredDrug, amountSelling);
            sellAmount = multiplierAmount >= 1 ? multiplierAmount * val : 0;

            if (sellAmount == 0){
                OxyPlugin.log("No Sell Amount for " + player.getName() + "'s sale of " + id);
                return false;
            }
            double mainValue = Math.max(sellMarket, sellAmount);
            if (intoDeposit){
                OxyPlugin.getEconomy().depositPlayer(player,mainValue);
                return true;
            }
        }
        return !intoDeposit&&sellAmount>0;
    }
}
