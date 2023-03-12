package com.thebetterchoice.oxyman.menu;

/**
 * Represents the price data for an item.
 */
public class ItemPriceData {

    private double basePrice;
    private double minPrice;
    private double maxPrice;
    private DemandFactors demandFactor;
    private SupplyFactors supplyFactor;

    public ItemPriceData(double basePrice, double minPrice, double maxPrice) {
        this.basePrice = basePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
//        this.demandFactor = demandFactor;
//        this.supplyFactor = supplyFactor;
    }

    /**
     * Gets the base price for this ItemPriceData.
     *
     * @return the base price
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the base price for this ItemPriceData.
     *
     * @param basePrice the new base price
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Gets the minimum price for this ItemPriceData.
     *
     * @return the minimum price
     */
    public double getMinPrice() {
        return minPrice;
    }

    /**
     * Sets the minimum price for this ItemPriceData.
     *
     * @param minPrice the new minimum price
     */
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    /**
     * Gets the maximum price for this ItemPriceData.
     *
     * @return the maximum price
     */
    public double getMaxPrice() {
        return maxPrice;
    }

    /**
     * Sets the maximum price for this ItemPriceData.
     *
     * @param maxPrice the new maximum price
     */
    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * Gets the demand factor for this ItemPriceData.
     *
     * @return the demand factor
     */
    public DemandFactors getDemandFactor() {
        return demandFactor;
    }

    public SupplyFactors getSupplyFactor() {
        return supplyFactor;
    }
}
