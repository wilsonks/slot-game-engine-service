package com.slotcentral.gameengine.paytable;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SymbolPayoutTable {
    private final Map<String, Integer> payouts = new HashMap<>();

    public SymbolPayoutTable() {
        payouts.put("SEVEN|SEVEN|SEVEN", 2000);
        payouts.put("BAR3|BAR3|BAR3", 500);
        payouts.put("BAR2|BAR2|BAR2", 200);
        payouts.put("BAR|BAR|BAR", 100);
        payouts.put("BELL|BELL|BELL", 100);
        payouts.put("PLUM|PLUM|PLUM", 40);
        payouts.put("ORANGE|ORANGE|ORANGE", 30);
        payouts.put("LEMON|LEMON|LEMON", 20);
        payouts.put("CHERRY|CHERRY|CHERRY", 20);
        payouts.put("BONUS|BONUS|BONUS", 50);
    }

    public Map<String, Integer> getPayouts() {
        return payouts;
    }
}
