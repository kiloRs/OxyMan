package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clickedItem = event.getCurrentItem();
        if (inventory == null || clickedItem == null) {
            return;
        }
        if (inventory.getHolder() instanceof ShopInventory shopInventory) {
            event.setCancelled(true); // prevent the player from taking the item out of the shop inventory
            if (event.getRawSlot() < inventory.getSize()) { // player clicked inside the shop inventory
                InventoryItem item = shopInventory.getSlot(event.getSlot());
                if (item == null) {
                    return;
                }
                double price = item.getCurrentPrice();
                int amount = clickedItem.getAmount();
                double total = price * amount;
                // check if the player has enough items to sell
                if (player.getInventory().containsAtLeast(clickedItem, amount)) {
                    // remove the items from the player's inventory
                    player.getInventory().removeItem(new ItemStack(clickedItem.getType(), amount));
                    // add the money to the player's balance
                    OxyPlugin.getEconomy().depositPlayer(player, total);
                    // update the shop inventory
                    shopInventory.update();
                    // notify the player
                    player.sendMessage(ChatColor.GREEN + "Sold " + amount + " " + clickedItem.getType().name() + " for $" + total);
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have enough items to sell.");
                }
            }
        }
    }
}
