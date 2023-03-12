package com.thebetterchoice.oxyman.utils;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.Stats;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.api.util.SmartGive;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SmartSell {

    private final Player player;
    private double sellPrice = 0;
    private boolean cancel;
    @Getter
    private final Map<Integer,ItemStack> store = new HashMap<>();
    private Map<Integer,ItemStack> selling;
    private final ItemStack individualItemstack;


    public SmartSell(Player player, ItemStack itemStack){
        this.player = player;
        this.cancel = false;
        if (itemStack == null || SellUtil.isValidForSale(itemStack) == null){
            throw new RuntimeException("Invalid SmartSell Input");
        }
        this.individualItemstack = itemStack;
        double nab = NBTItem.get(individualItemstack).getDouble(Stats.currentPrice.getNBTPath());
        this.sellPrice = nab * individualItemstack.getAmount();

//        double value = NBTItem.get(individualItemstack).getDouble(Stats.currentPrice.getNBTPath());
//        this.sellPrice = value>ConfigReference.serverMaxCost?ConfigReference.serverMaxCost: Math.max(value, ConfigReference.serverMinCost);


    }

//    private void loadSelling(ItemStack itemStack) {
//        for (ItemStack content : iMenu.getInventory().getContents()) {
//            if (content == null){
//                continue;
//            }
//             if (!NBTItem.get(content).hasType()){
//                 continue;
//             }
//             String mmoitem_item_id = NBTItem.get(content).getString("MMOITEMS_ITEM_ID");
//             if (!NBTItem.get(content).getType().equalsIgnoreCase(Stats.DRUG.getId())){
//                 continue;
//             }
//            double v = NBTItem.get(content).getDouble(Stats.currentPrice.getNBTPath());
//            double vv = NBTItem.get(itemStack).getDouble(Stats.currentPrice.getNBTPath());
//            if (!new EqualsBuilder().append(mmoitem_item_id, NBTItem.get(itemStack).getString("MMOITEMS_ITEM_ID")).append(content.getAmount(), itemStack.getAmount()).append(v, vv).isEquals()){
//                continue;
//            }
//            selling.put(iMenu.getInventory().first(content),content);
//        }
//    }

    /**
     * @param player Command Sender
     */
    public SmartSell(Player player){
        this.player = player;
        this.selling = new HashMap<>();
        LuckPerms lp = LuckPermsProvider.get();
        this.cancel = !lp.getPlayerAdapter(Player.class).getPermissionData(player).checkPermission("Oxy.Sell.Smart").asBoolean();
        this.individualItemstack = null;


    }
    public SmartSell(Player player, Map<Integer,ItemStack> sell){
        this.player = player;
        this.cancel = false;
        this.selling = sell;
        this.individualItemstack = null;

    }

    public SmartSell(InventoryCloseEvent event){
        this.player = ((Player) event.getPlayer());
        this.cancel = ((Player) event.getPlayer()).isSneaking();
        this.selling = new HashMap<>();
        this.individualItemstack = null;
    }


    public boolean shouldCancel(){return cancel;}
    public void setCancel(boolean c){
        this.cancel = c;
    }
    public int prepareIndividualSale(ItemStack itemStack){
        ItemStack obtained = store.putIfAbsent(store.size(), itemStack);

        if (obtained == null){
            throw new RuntimeException("Invalid Sell");
        }
        RegisteredDrug validForSale = SellUtil.isValidForSale(obtained);

        if (validForSale == null){
            OxyLogger.sendError("No Valid Registered Drug...");
            return -1;
        }

        return store.size();
    }

    private void clearPrice(){
        sellPrice = 0;
    }


    public void sellIndividual(){
        if (cancel){
            OxyLogger.error("Cancelled Individual Sale...");
            return;
        }
        if (selling.isEmpty()){
            OxyLogger.sendError("Bad Individual Sale");
            return;
        }

        for (Map.Entry<Integer, ItemStack> entry : selling.entrySet()) {
            Integer integer = entry.getKey();
            ItemStack itemStack = entry.getValue();

            double value = NBTItem.get(itemStack).getDouble(Stats.currentPrice.getNBTPath());
            sellPrice = sellPrice + value;
        }

        OxyPlugin.getEconomy().depositPlayer(player,sellPrice);
    }
    public void sell(){
        if (shouldCancel()){
            OxyPlugin.send(player,"Cancelling Sale!");
            giveDrops();
            return;
        }
        giveDrops();
        giveMoney(OxyUser.getStored(player).getBonusHandler().getCurrentBonus());
        OxyPlugin.send(player,"Sending Money to " + player.getName() + " (" + sellPrice + ")");
    }

    public void giveDrops(){
        player.closeInventory();
        player.sendTitle("Drops!","Sending Sale Drops",10,100,10);
        for (Map.Entry<Integer, ItemStack> entry : selling.entrySet()) {
            Integer integer = entry.getKey();
            ItemStack itemStack = entry.getValue();
            if (cancel){
                new SmartGive(player).give(itemStack);
                continue;
            }
            if (!store.containsKey(integer) || !store.get(integer).equals(itemStack)) {
                new SmartGive(player).give(itemStack);
            }
        }
        //Technically, the store stores the good sell items, the other stores ALL sell items.
        //SO above, removing the bad items, should leave the same sized maps.

        if (store.size()!=selling.size()){
            OxyPlugin.log("Invalid Sell-Store Sizing! " + store.size() + " " + selling.size() + "!");
        }
    }
    public void giveMoney(double playersTotalMultiplier){
        if (!shouldCancel()) {
            OxyPlugin.getEconomy().depositPlayer(player, sellPrice * playersTotalMultiplier);
            OxyPlugin.send(player,"&aGiving you " + sellPrice + " for your sale!");
        }
        else {
            OxyPlugin.send(player, "&aCancelling Sale!!");
            return;
        }
        OxyPlugin.send(player,"&eSuccessfully Sent!");
    }

}
