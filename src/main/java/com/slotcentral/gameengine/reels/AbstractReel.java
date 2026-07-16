package com.slotcentral.gameengine.reels;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;

import java.util.List;

public abstract class AbstractReel implements ReelComponent {
    protected final List<String> strip;

    protected AbstractReel(List<String> strip) {
        this.strip = strip;
    }

    @Override
    public String getSymbol(int index) {
        int i = ((index - 1) % strip.size() + strip.size()) % strip.size();
        return strip.get(i);
    }

    @Override
    public int getStopIndex(int betIndex, RandomNumberGenerator rng) {
        return rng.nextReelPos();
    }

    @Override
    public List<String> getStrip() {
        return strip;
    }
}
