package com.thebetterchoice.oxyman.menu;

import java.util.HashMap;
import java.util.Map;

public class DemandFactors {
    private final Map<String, Double> factors = new HashMap<>();
    private final String factor;

    public DemandFactors(String factor) {

        this.factor = factor;
    }

    public static DemandFactors getInstance(String f) {
        return new DemandFactors(f);
    }

    public void setFactor( double factorAmount) {
        factors.put(factor, factorAmount);
    }

    public double getFactor() {
        return factors.getOrDefault(factor, 1.0);
    }
}
