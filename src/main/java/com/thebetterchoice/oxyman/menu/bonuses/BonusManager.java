package com.thebetterchoice.oxyman.menu.bonuses;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages active bonuses for players.
 */
public class BonusManager {

    // A map of active bonuses for each player
    private final Map<UUID, Map<Bonus, Long>> activeBonuses;

    // A timer for expiring timed bonuses
    private final BonusTimer bonusTimer;

    /**
     * Creates a new BonusManager.
     * Initializes the activeBonuses map and starts the bonusTimer.
     */
    public BonusManager() {
        activeBonuses = new HashMap<>();
        bonusTimer = new BonusTimer(this);
        bonusTimer.start();
    }

    /**
     * Gets the active bonus of a specific type for a player.
     * @param uuid the UUID of the player
     * @param bonusType the type of bonus to look for
     * @return the active bonus of the given type for the player, or null if none is found
     */
    public Bonus getActiveBonus(UUID uuid, BonusType bonusType) {
        return activeBonuses.values().stream()
                .flatMap(bonuses -> bonuses.entrySet().stream())
                .filter(entry -> entry.getKey().getPlayerUUID().equals(uuid) &&
                        entry.getKey().getBonusType() == bonusType &&
                        entry.getKey().isActive())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }


    /**
     * Adds a bonus to a player's active bonuses.
     * @param playerId the UUID of the player
     * @param bonus the bonus to add
     */
    public void addBonus(UUID playerId, Bonus bonus) {
        Map<Bonus, Long> playerBonuses = activeBonuses.computeIfAbsent(playerId, k -> new HashMap<>());
        playerBonuses.put(bonus, System.currentTimeMillis());
    }

    /**
     * Removes a bonus from a player's active bonuses.
     * @param playerId the UUID of the player
     * @param bonus the bonus to remove
     */
    public void removeBonus(UUID playerId, Bonus bonus) {
        Map<Bonus, Long> playerBonuses = activeBonuses.get(playerId);
        if (playerBonuses != null) {
            playerBonuses.remove(bonus);
            if (playerBonuses.isEmpty()) {
                activeBonuses.remove(playerId);
            }
        }
    }

    /**
     * Gets the bonus of a specific type for a player.
     * @param uuid the UUID of the player
     * @param bonusType the type of bonus to look for
     * @return the bonus of the given type for the player, or null if none is found
     */
    public Bonus getBonus(UUID uuid, BonusType bonusType) {
        for (Map<Bonus, Long> playerBonuses : activeBonuses.values()) {
            for (Bonus bonus : playerBonuses.keySet()) {
                if (bonus.getPlayerUUID().equals(uuid) && bonus.getBonusType() == bonusType) {
                    return bonus;
                }
            }
        }
        return null;
    }

    /**
     * @param playerUUID Player
     * This is used for the top of the menu, showing the players current bonus amount at all times, when looking into the shop!
     * @return the total bonus percentage active for the player
     */
    public double getTotalBonusPercentage(UUID playerUUID) {
        double totalBonusPercentage = 0;

        for (BonusType type : BonusType.values()) {
            Bonus bonus = getBonus(playerUUID, type);
            if (bonus != null && bonus.isActive()) {
                if (bonus.isTimed()) {
                    double timeRemaining = bonus.getSecondsLeft();
                    if (timeRemaining <= 0) {
                        bonus.setActive(false);
                        continue;
                    }
                }
                totalBonusPercentage += bonus.getBonusPercentage();
            }
        }

        return totalBonusPercentage;
    }

    /**
     * Returns the active bonuses for all players.
     * @return the map of active bonuses for each player
     */
    public Map<UUID, Map<Bonus, Long>> getActiveBonuses() {
        return activeBonuses;
    }

    /**
     * Returns the bonus timer used to check for expired bonuses.
     * @return the bonus timer
     */
    public BonusTimer getBonusTimer() {
        return bonusTimer;
    }

    /**
     * Updates the duration of a bonus for a player.
     * @param playerId the UUID of the player
     * @param bonus the bonus to update
     * @param duration the new duration in seconds
     */
    public void updateBonusDuration(UUID playerId, Bonus bonus, long duration) {
        Map<Bonus, Long> playerBonuses = activeBonuses.computeIfAbsent(playerId, k -> new HashMap<>());
        playerBonuses.put(bonus, duration);
    }
}