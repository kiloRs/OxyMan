package com.thebetterchoice.oxyman.specialevents;

import com.thebetterchoice.oxyman.sales.Market;

public interface SpecialEventListener {
    void onEvent(SpecialEvent specialEvent, int val);
    Market.EventReset onEnd(SpecialEvent event, long value, double multiplier);
    void announce(String announceSomething);
}
