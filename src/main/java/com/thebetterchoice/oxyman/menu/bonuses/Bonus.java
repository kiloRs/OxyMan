package com.thebetterchoice.oxyman.menu.bonuses;

import java.util.UUID;

public abstract class Bonus {
    // Player UUID for this bonus
    private final UUID playerUUID;

    private BonusType type;
    // Bonus percentage to apply to the player
    private final double bonusPercentage;

    // Start time of the bonus in milliseconds
    private final long startTime;

    // Duration of the bonus in seconds (-1 for no duration)
    private int duration;

    // Active state of the bonus
    private boolean active;
    private BonusManager bonusManager;

    /**
     * Constructor for a Bonus object.
     * @param playerUUID UUID of the player to apply the bonus to.
     * @param bonusPercentage Percentage of the bonus to apply to the player.
     * @param duration Duration of the bonus in seconds (-1 for no duration).
     */
    public Bonus(BonusManager bonusManager,UUID playerUUID, BonusType type, double bonusPercentage, int duration) {
        this.bonusManager = bonusManager;
        this.playerUUID = playerUUID;
        this.type = type;
        this.bonusPercentage = bonusPercentage;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        this.active = duration == 0 || !isExpired();
    }

    /**
     * Returns the UUID of the player associated with this bonus.
     * @return The UUID of the player.
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Returns the percentage of the bonus to apply to the player.
     * @return The bonus percentage.
     */
    public double getBonusPercentage() {
        return bonusPercentage;
    }

    /**
     * Returns the start time of the bonus in milliseconds.
     * @return The start time of the bonus.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns the duration of the bonus in seconds.
     * @return The duration of the bonus.
     */
    public int getDuration() {
        return duration;
    }

    public abstract String getDescription();

    public abstract boolean checkCriteria();

    /**
     * Returns whether or not the bonus is currently active.
     * @return True if the bonus is active, false otherwise.
     */
    public boolean isActive() {
        return !isExpired();
    }

    /**
     * Returns whether or not the bonus has a duration.
     * @return True if the bonus has a duration, false otherwise.
     */
    public boolean isTimed() {
        return duration >= 0;
    }

    /**
     * Returns whether or not the bonus has expired.
     * @return True if the bonus has expired, false otherwise.
     */
    public boolean isExpired() {
        if (!isTimed()) {
            return false;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime >= (duration * 1000L);
    }

    /**
     * Returns the number of seconds left until the bonus expires.
     * @return The number of seconds left, or -1 if the bonus has no duration.
     */
    public double getSecondsLeft() {
        if (!isTimed()) {
            return -1;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        long secondsLeft = (duration * 1000L - elapsedTime) / 1000;
        return secondsLeft > 0 ? secondsLeft : 0;
    }

/**
 * Adds a specified number of seconds to the duration of the bonus.
 * If the bonus has no duration, this method does nothing.
 * If the new duration is negative, it is set to 0.
 * If the bonus is active, the corresponding entry in the
**/

    public void addSeconds(int seconds) {
        if (duration == -1) {
            return; // Bonus has no duration, do nothing
        }
        duration += seconds; // Add seconds to duration
        if (duration < 0) {
            duration = 0; // Ensure duration cannot be negative
        }
        if (isActive()) {
            // Update corresponding entry in activeBonuses map with new duration
            bonusManager.updateBonusDuration(playerUUID, this, duration);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BonusType getBonusType() {
        return type;
    }
}