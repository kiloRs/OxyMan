package com.thebetterchoice.oxyman.menu;

import com.thebetterchoice.oxyman.OxyPlugin;
import lombok.Getter;

public class Save implements Runnable {

    @Getter
    private final PriceConfig priceConfig = new PriceConfig();

    public Save() {

    }

    @Override
    public void run() {
        priceConfig.save();
        OxyPlugin.debug("Running Save of Information to Files!");
    }

}

