package com.thebetterchoice.oxyman.menu.bonuses;

import java.util.Map;
import java.util.UUID;

public class BonusTimer implements Runnable {
    private final BonusManager bonusManager;
    private boolean running;
    private final int tickDelay = 1000; // in milliseconds

    public BonusTimer(BonusManager bonusManager) {
        this.bonusManager = bonusManager;
        this.running = true;
    }

    public void run() {
        if (Thread.currentThread().getName().equals("main")) {
            throw new IllegalStateException("BonusTimer cannot be run on the main thread!");
        }

        while (running) {
            try {
                //Unsure about this still.
                Thread.sleep(tickDelay);
                Map<UUID, Map<Bonus, Long>> activeBonuses = bonusManager.getActiveBonuses();
                for (UUID playerId : activeBonuses.keySet()) {
                    Map<Bonus, Long> playerBonuses = activeBonuses.get(playerId);
                    for (Bonus bonus : playerBonuses.keySet()) {
                        if (bonus.isExpired()) {
                            bonusManager.removeBonus(playerId, bonus);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopTimer() {
        running = false;
    }


    public void start(){
        run();
    }
}
