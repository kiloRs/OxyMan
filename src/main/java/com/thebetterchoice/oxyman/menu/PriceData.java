package com.thebetterchoice.oxyman.menu;

public class PriceData {
    private final double price;
    private final int sales;
    private final SupplyFactors supply;
    private final DemandFactors demand;

    public PriceData(double price, int sales, SupplyFactors supply, DemandFactors demand) {
        this.price = price;
        this.sales = sales;
        this.supply = supply;
        this.demand = demand;
    }

    public double getPrice() {
        return price;
    }

    public int getSales() {
        return sales;
    }

    public double getSupply() {
        return supply.getFactor();
    }

    public double getDemand() {
        return demand.getFactor();
    }
}
