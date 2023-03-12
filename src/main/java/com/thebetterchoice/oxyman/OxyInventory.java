package com.thebetterchoice.oxyman;

import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.Type;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class OxyInventory {



    public RegisteredDrug getDrug(int slot){
        if (!isDrug(slot)){
            return null;
        }

        return getDrug(slot);
    }
    private boolean is(int slot, Type type){
        ItemStack item = getInventory().getItem(slot);
        NBTItem nbtItem = NBTItem.get(item);
        return item != null && nbtItem.hasType() && nbtItem.getType().equalsIgnoreCase(type.getId());
    }
    public boolean isBonus(int slot){
        return is(slot,Stats.BONUSES);
    }
    public boolean isNavigation(int slot){
        return is(slot,Stats.NAVIGATION);
    }
    public boolean isDrug(int slot){
        return is(slot,Stats.DRUG);
    }
    public boolean isAvailable(int slot){
        ItemStack item = getInventory().getItem(slot);

        if (item == null){
            return false;
        }
        if (!NBTItem.get(item).hasType()){
            return false;
        }
        return NBTItem.get(item).getType().equalsIgnoreCase(Stats.DRUG.getId());
    }

    public abstract void loadItemsToMap();
    public abstract void loadItemsFromMap();


    @NotNull
    public Inventory getInventory() {
        return Bukkit.createInventory(OxyPlugin.getMarket(), InventoryType.CHEST,"Oxy");
    }
}
