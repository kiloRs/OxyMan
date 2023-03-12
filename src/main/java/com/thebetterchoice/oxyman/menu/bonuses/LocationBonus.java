package com.thebetterchoice.oxyman.menu.bonuses;

import com.thebetterchoice.oxyman.OxyPlugin;

import java.util.UUID;

/**
 * Represents a location-based bonus that applies to a player when they are within a certain distance of a specified coordinate.
 */
public class LocationBonus extends Bonus {
    private final double x;
    private final double y;
    private final double z;
    private final double radius;

    /**
     * Creates a new LocationBonus with the specified parameters.
     * @param playerUUID The UUID of the player who will receive the bonus.
     * @param bonusPercentage The percentage bonus that will be applied to the player's selling price.
     * @param duration The duration of the bonus in milliseconds.
     * @param x The x-coordinate of the center of the bonus area.
     * @param y The y-coordinate of the center of the bonus area.
     * @param z The z-coordinate of the center of the bonus area.
     * @param radius The radius of the bonus area in blocks.
     */
    public LocationBonus(UUID playerUUID, double bonusPercentage,int duration, double x, double y, double z, double radius) {
        super(OxyPlugin.getBonusManager(),playerUUID, BonusType.LOCATION, bonusPercentage, (int) duration);
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    /**
     * Checks if the specified player is within the bonus area.
     * @param playerX The x-coordinate of the player.
     * @param playerY The y-coordinate of the player.
     * @param playerZ The z-coordinate of the player.
     * @return true if the player is within the bonus area, false otherwise.
     */
    public boolean isPlayerInBonusArea(double playerX, double playerY, double playerZ) {
        double distance = Math.sqrt(Math.pow(playerX - x, 2) + Math.pow(playerY - y, 2) + Math.pow(playerZ - z, 2));
        return distance <= radius;
    }
}