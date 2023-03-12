package com.thebetterchoice.oxyman.menu;

import java.util.HashMap;
import java.util.Map;

public class SupplyFactors {
    private static final Map<String, Double> factors = new HashMap<>();
    private final String factor;

    public static SupplyFactors getInstance(String f) {
        return new SupplyFactors(f);
    }
    public SupplyFactors(String factor){
        this.factor = factor;
        factors.putIfAbsent(factor,1.0);
    }

    public void setFactor(double factorAmount) {
        factors.put(factor, factorAmount);
    }

    public double getFactor() {
        return factors.getOrDefault(factor, 1.0);
    }
}
