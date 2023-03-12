package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShopInventory implements InventoryHolder {

    private final UUID ownerUUID;
    private final Inventory inventory;
    private final InventoryItemManager inventoryItemManager;
    // map to store the inventory items by slot index
    private final Map<Integer, InventoryItem> inventoryItemsBySlot;

        public ShopInventory(UUID ownerUUID, InventoryItemManager inventoryItemManager) {
            this.ownerUUID = ownerUUID;
            this.inventory = Bukkit.createInventory(this, 54, ChatColor.BLUE + "Shop");
            this.inventoryItemManager = inventoryItemManager;

            // initialize the inventory items map
            inventoryItemsBySlot = new HashMap<>();
            for (int i = 0; i < inventory.getSize(); i++) {
                InventoryItem inventoryItem = InventoryItemManager.getItems().get(i);
                inventoryItemsBySlot.put(i, inventoryItem);
            }

            update();
        }

    public void update() {
        inventory.clear();
        for (int i = 0; i < inventory.getSize(); i++) {
            InventoryItem inventoryItem = inventoryItemManager.getSlot(i);
            ItemStack itemStack = inventoryItem.toItemStack(inventory.getItem(i).getAmount());
            ItemMeta itemMeta = itemStack.getItemMeta();

            // Set display name
            itemMeta.setDisplayName(ChatColor.BLUE + inventoryItem.getDisplayName());

            // Set lore
            List<String> lore = new ArrayList<>();
            double basePrice = inventoryItem.getPriceData().getBasePrice();
            double minPrice = inventoryItem.getPriceData().getMinPrice();
            double maxPrice = inventoryItem.getPriceData().getMaxPrice();
            double demandFactor = inventoryItem.getPriceData().getDemandFactor();
            double supplyFactor = inventoryItem.getPriceData().getSupplyFactor();
            lore.add(ChatColor.GRAY + String.format("Base Price: %.2f", basePrice));
            lore.add(ChatColor.GRAY + String.format("Minimum Price: %.2f", minPrice));
            lore.add(ChatColor.GRAY + String.format("Maximum Price: %.2f", maxPrice));
            lore.add(ChatColor.GRAY + String.format("Demand Factor: %.2f", demandFactor));
            lore.add(ChatColor.GRAY + String.format("Supply Factor: %.2f", supplyFactor));
            itemMeta.setLore(lore);

            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i, itemStack);
        }
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory()
    {
        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == inventory) {
            event.setCancelled(true);

            int slot = event.getSlot();
            InventoryItem inventoryItem = inventoryItemManager.getSlot(slot);
            if (inventoryItem != null) {
                if (event.getClick() == ClickType.LEFT) {
                    sellItem(player, inventoryItem);
                }
            }
        }
    }

    private void sellItem(Player player, InventoryItem inventoryItem) {
        ItemStack itemStack = inventoryItem.toItemStack(1);
        double basePrice = inventoryItem.getPriceData().getBasePrice();
        double minPrice = inventoryItem.getPriceData().getMinPrice();
        double maxPrice = inventoryItem.getPriceData().getMaxPrice();
        double demandFactor = inventoryItem.getPriceData().getDemandFactor();
        double supplyFactor = inventoryItem.getPriceData().getSupplyFactor();

        Inventory playerInventory = player.getInventory();
        int amountToSell = 0;
        for (ItemStack stack : playerInventory.getContents()) {
            if (stack == null || stack.getType() == Material.AIR) {
                continue;
            }
            if (stack.isSimilar(itemStack)) {
                amountToSell += stack.getAmount();
            }
        }
        if (amountToSell == 0) {
            player.sendMessage(ChatColor.RED + "You don't have any of this item to sell.");
            return;
        }

        double totalPrice = calculatePrice(basePrice, minPrice, maxPrice, demandFactor, supplyFactor, amountToSell);
        ItemStack soldItemStack = itemStack.clone();
        soldItemStack.setAmount(amountToSell);

        boolean success = playerInventory.removeItem(soldItemStack).isEmpty();
        if (success) {
            EconomyResponse response = OxyPlugin.getEconomy().depositPlayer(player, totalPrice);
            if (response.transactionSuccess()) {
                player.sendMessage(ChatColor.GREEN + String.format("Sold %d x %s for %.2f %s.", amountToSell,
                        itemStack.getType().name(), response.amount, OxyPlugin.getEconomy().currencyNamePlural()));
                update();
            } else {
                player.sendMessage(ChatColor.RED + "An error occurred while trying to sell the item.");
                playerInventory.addItem(soldItemStack);
            }
        } else {
            player.sendMessage(ChatColor.RED + "You don't have enough inventory space to sell the item.");
        }
    }

    private double calculatePrice(double basePrice, double minPrice, double maxPrice, double demandFactor, double supplyFactor, int amount) {
        double price = basePrice;
        if (demandFactor != 0) {
            price += Math.min(demandFactor * amount, 1) * basePrice;
        }
        if (supplyFactor != 0) {
            price -= Math.min(supplyFactor * amount, 1) * basePrice;
        }
        price = Math.max(price, minPrice);
        price = Math.min(price, maxPrice);
        return price * amount;
    }
}
