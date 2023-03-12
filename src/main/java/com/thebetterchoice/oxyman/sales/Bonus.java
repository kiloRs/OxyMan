package com.thebetterchoice.oxyman.sales;

import lombok.Getter;
import lombok.Setter;

/**
 * A bonus instance! Base Instance
 */
public class Bonus {

    @Getter
    private final double amount;
    @Getter
    @Setter
    private long length;

    public Bonus(double amount, long length){
        this.amount = amount;
        this.length = length;
    }

    public String getFormattedTime() {
        int hours = Math.toIntExact(this.length / 60);
        int min = Math.toIntExact(this.length % 60);
        String var10000 = String.format("%02d", hours);
        return var10000 + ":" + String.format("%02d", min) + " (HH:MM)";
    }
}
