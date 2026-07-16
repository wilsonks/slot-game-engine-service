package com.slotcentral.gameengine.paytable;

import org.springframework.stereotype.Service;

@Service
public class PaytableService {
    private final PaytableConfig config;

    public PaytableService(PaytableConfig config) {
        this.config = config;
    }

    public int getBetAmount(int betIndex, int denomIndex) {
        return config.getBetValues().get(betIndex).get(denomIndex);
    }

    public double getJackpotContribution(int betIndex, int denomIndex) {
        return getBetAmount(betIndex, denomIndex) * config.getJackpotContributionRate();
    }

    public PaytableConfig getConfig() {
        return config;
    }
}
