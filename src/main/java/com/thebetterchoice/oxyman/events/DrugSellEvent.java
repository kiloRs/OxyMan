package com.thebetterchoice.oxyman.events;

import com.thebetterchoice.oxyman.OxyPlugin;
import com.thebetterchoice.oxyman.OxyUser;
import com.thebetterchoice.oxyman.drugs.RegisteredDrug;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class DrugSellEvent extends Event implements Cancellable {
    private final Inventory closing;
    @Getter
    private RegisteredDrug registered;
    @Getter
    private OxyUser player;
    @Getter
    @Setter
    private double amountOfItems;
    @Getter
    @Setter
    private double cost;
    private boolean cancelled;



    public DrugSellEvent(RegisteredDrug drug, Integer amountSelling, OxyUser oxyUser){
        this(null,drug,amountSelling,oxyUser);
    }
    public DrugSellEvent(Inventory closing, RegisteredDrug registered, Integer amountOfItems, OxyUser player)  {
        this.closing = closing;

        if (this.closing == null){
            OxyPlugin.send((CommandSender) player,"Invalid Inventory!");
            return;
        }
        this.registered = registered;
        this.amountOfItems = Double.valueOf(amountOfItems);
        this.player = player;
        this.cost = registered.getCurrentCost();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList(){
        return new HandlerList();
    }

}
