package com.thebetterchoice.oxyman.listener;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import com.thebetterchoice.oxyman.events.DrugSellEvent;
import com.thebetterchoice.oxyman.events.OpenShopEvent;
import com.thebetterchoice.oxyman.menu.MenuInstance;
import com.thebetterchoice.oxyman.utils.ConfigReference;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class DrugListener implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent e){
        if (e.getInventory().getHolder() instanceof MenuInstance menuInstance){
            OxyPlugin.debug("OPENING OXY MENU INSTANCE!");
            return;
        }

    }

    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = false)
    public void onStart(OpenShopEvent e){
        if (e.isCancelled()) {
            OxyPlugin.log("Cancelling Open Shop Event!");
            return;
        }
        OxyPlugin.log("Shop Opening!");
    }
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onSell(DrugSellEvent event){
        RegisteredDrug drug = event.getRegistered();

        double currentBonus = event.getPlayer().getBonusHandler().getCurrentBonus();
        if (currentBonus>0){
            double currentCost = drug.getCurrentCost();
            if (currentCost>0){

                if (currentCost> ConfigReference.serverMaxCost){
                    event.setCost(ConfigReference.serverMaxCost);
                }

            }

        }
    }
}
