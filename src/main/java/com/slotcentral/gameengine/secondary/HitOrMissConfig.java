package com.slotcentral.gameengine.secondary;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hit-or-miss")
public class HitOrMissConfig {
    private double hitProbability = 0.45;
    private int baseWin = 10;
    private int multiplier = 3;

    public double getHitProbability() {
        return hitProbability;
    }

    public void setHitProbability(double hitProbability) {
        this.hitProbability = hitProbability;
    }

    public int getBaseWin() {
        return baseWin;
    }

    public void setBaseWin(int baseWin) {
        this.baseWin = baseWin;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
