package com.thebetterchoice.oxyman.menu.bonuses;


import com.thebetterchoice.oxyman.OxyPlugin;
import org.bukkit.entity.Player;

/**
 * A bonus based around a player selling over 100, 1000, or 10,000 of an item, giving three stages of bonus to them.
 */
public class SaleBonus extends Bonus {

    private final int targetAmount;
    private final double bonusPercentage;

    public SaleBonus(Player player, int targetAmount, double bonusPercentage) {
        super(OxyPlugin.getBonusManager(),player.getUniqueId(), BonusType.VOUCHER,bonusPercentage,-1);
        this.targetAmount = targetAmount;
        this.bonusPercentage = bonusPercentage;
    }

    @Override
    public double getBonusPercentage() {
        return bonusPercentage;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    @Override
    public String getDescription() {
        return String.format("Sell %d of any item to activate. Increases selling price by %.0f%%", targetAmount, bonusPercentage * 100);
    }

    @Override
    public boolean checkCriteria() {
        int soldAmount = 0
        // code to get amount of items sold by player

        return soldAmount >= targetAmount;
    }

    @Override
    public boolean isActive() {
        return checkCriteria();
    }

}