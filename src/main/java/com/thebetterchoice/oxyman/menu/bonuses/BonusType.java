package com.thebetterchoice.oxyman.menu.bonuses;

public enum BonusType {
    PERMISSION(false),
    OTHER(false),
    UNKNOWN(false),
    LOCATION(true),
    SPECIAL(true),
    VOUCHER(false),
    UNLOCK(true);

    private final boolean isTimed;

    BonusType(boolean isTimed) {
        this.isTimed = isTimed;
    }

    public boolean isTimed() {
        return isTimed;
    }
}