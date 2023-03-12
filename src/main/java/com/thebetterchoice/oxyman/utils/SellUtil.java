package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.DrugRegistry;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class SellUtil {
    @Nullable
    public static RegisteredDrug isValidForSale(ItemStack itemStack) {
        if (itemStack == null){
            return null;
        }
        if (itemStack.getType() == Material.AIR){
            return null;
        }
        NBTItem nbt = NBTItem.get(itemStack);
        if (!nbt.hasType()){
            return null;
        }
        if (!DrugRegistry.hasDrug(MMOItems.getID(nbt))){
            if (NBTItem.get(itemStack).getType().equalsIgnoreCase(Stats.DRUG.getId())){
                DrugRegistry.register(Stats.DRUG.getId(),MMOItems.getID(nbt));
            }
            return DrugRegistry.getRegisteredDrugs().get(MMOItems.getID(nbt));
        }
        Type type = getType(itemStack);

        if (type == null){
            return null;
        }
        if (Stats.DRUG.equals(type)){
            return DrugRegistry.getRegisteredDrugs().get(MMOItems.getID(nbt));
        }
        return null;
    }

    public static Type getType(ItemStack itemStack){
        NBTItem nbtItem = NBTItem.get(itemStack);
        return nbtItem.hasType()? MMOItems.getType(nbtItem):null;
    }
    public static void sortDrugs(List<RegisteredDrug> drugs, boolean ascending) {
        Comparator<RegisteredDrug> comparator = (c1, c2) -> ascending ? (int)(OxyPlugin.getMarket().sell(c2, 1.0) - OxyPlugin.getMarket().sell(c1, 1.0)) : (int)(OxyPlugin.getMarket().sell(c1, 1.0) - OxyPlugin.getMarket().sell(c2, 1.0));
        drugs.sort(comparator);
    }
    public static SellType findSellType(Type type) {
        SellType sellType = SellType.NONE;
        if (type == null){
            sellType = SellType.VANILLA;
        }
        else if (type.equals(Stats.BONUSES)){
            sellType = SellType.BONUSES;
        }
        else if (type.equals(Stats.DRUG)){
            sellType = SellType.DRUG;
        }
        else if (type.equals(Stats.NAVIGATION)){
            sellType = SellType.NAVIGATION;
        }
        else {
            sellType = SellType.UNKNOWN;
        }

        return sellType;
    }
    public enum SellType{
        DRUG,NAVIGATION,VANILLA,NONE, BONUSES, UNKNOWN
    }
}

