package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomInventory {
    private final List<InventoryItem> items;
    private final Inventory inventory;

    public CustomInventory(String title, int size) {
        items = new ArrayList<>();
        inventory = Bukkit.createInventory(null, size, title);
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public void removeItem(InventoryItem item) {
        items.remove(item);
    }

    public void display(Player player) {
        for (InventoryItem item : items) {
            ItemStack stack = item.toItemStack(1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(item.getDisplayName());
            meta.setLore(item.getLore());
            stack.setItemMeta(meta);
            inventory.addItem(stack);
        }
        player.openInventory(inventory);
    }

    public void updatePrices(double newPrice) {
        for (InventoryItem item : items) {
            // Update the price of the item based on the supply and demand equation
            item.setBasePrice(newPrice);
            ItemStack stack = item.toItemStack(1);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(item.getDisplayName());
            meta.setLore(List.of("Price: $" + item.getBasePrice()));
            stack.setItemMeta(meta);
            int slot = items.indexOf(item);
            inventory.setItem(slot, stack);
        }
    }

    public void setPrice(InventoryItem item, double newPrice) {
        item.setBasePrice(newPrice);
        OxyPlugin.debug("Price change to : " + newPrice + " for " + item.getDisplayName());
    }
}
