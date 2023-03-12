package com.thebetterchoice.oxyman.menu;

public class InventoryItemSettings {
    private final double basePrice;
    private final double minimumPrice;
    private final double maximumPrice;
    private final int saveIntervalMinutes;

    public InventoryItemSettings(double basePrice,double minimumPrice, double maximumPrice, int saveIntervalMinutes) {
        this.basePrice = basePrice;
        this.minimumPrice = minimumPrice;
        this.maximumPrice = maximumPrice;
        this.saveIntervalMinutes = saveIntervalMinutes;
    }

    public double getMinimumPrice() {
        return minimumPrice;
    }

    public double getMaximumPrice() {
        return maximumPrice;
    }

    public int getSaveIntervalMinutes() {
        return saveIntervalMinutes;
    }

    public double getBasePrice() {
        return basePrice;
    }
}