package com.thebetterchoice.oxyman.events;

import com.thebetterchoice.oxyman.OxyUser;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

public class OpenShopEvent extends InventoryOpenEvent implements Cancellable {
    @Getter
    private OxyUser user;

    public OpenShopEvent(@NotNull InventoryView transaction, OxyUser user) {
        super(transaction);
        this.user = user;
    }

    @Override
    public void setCancelled(boolean cancel) {
        super.setCancelled(cancel);
    }

    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }
}
