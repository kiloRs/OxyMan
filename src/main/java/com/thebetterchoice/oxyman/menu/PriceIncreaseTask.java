package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class PriceIncreaseTask implements Runnable {
    private final CustomInventory shopInventory;
    private final InventoryItemManager itemManager;
    private final double increaseAmount;
    private final long delay;
    private final long period;
    private final Map<InventoryItem, Long> timeSinceLastDecrease;

    public PriceIncreaseTask(CustomInventory shopInventory, InventoryItemManager itemManager, double increaseAmount, long delay, long period) {
        this.shopInventory = shopInventory;
        this.itemManager = itemManager;
        this.increaseAmount = increaseAmount;
        this.delay = delay;
        this.period = period;
        this.timeSinceLastDecrease = new HashMap<>();
    }

    @Override
    public void run() {
        for (InventoryItem item : InventoryItemManager.getItems()) {
            double currentPrice = item.getBasePrice();
            long timeSinceDecrease = getTimeSinceLastDecrease(item);
            double newPrice = currentPrice + getIncreaseAmount(timeSinceDecrease);
            if (newPrice > item.getPriceData().getMaxPrice()) {
                newPrice = item.getPriceData().getMaxPrice();
                resetTimeSinceLastDecrease(item);
            } else {
                updateTimeSinceLastDecrease(item);
            }
            shopInventory.setPrice(item, newPrice);
        }
    }

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(OxyPlugin.getPlugin(), this, delay, period);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTasks(OxyPlugin.getPlugin());
    }

    private long getTimeSinceLastDecrease(InventoryItem item) {
        if (!timeSinceLastDecrease.containsKey(item)) {
            timeSinceLastDecrease.put(item, 0L);
        }
        return System.currentTimeMillis() - timeSinceLastDecrease.get(item);
    }

    private void updateTimeSinceLastDecrease(InventoryItem item) {
        timeSinceLastDecrease.put(item, System.currentTimeMillis());
    }

    private void resetTimeSinceLastDecrease(InventoryItem item) {
        timeSinceLastDecrease.put(item, 0L);
    }

    private double getIncreaseAmount(long timeSinceDecrease) {
        // Increase the price by a fraction of the increase amount per minute
        return increaseAmount * (timeSinceDecrease / (60 * 6 * 1000.0));
    }
}