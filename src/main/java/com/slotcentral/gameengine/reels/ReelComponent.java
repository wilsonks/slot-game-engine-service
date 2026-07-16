package com.slotcentral.gameengine.reels;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;

import java.util.List;

public interface ReelComponent {
    String getSymbol(int index);

    int getStopIndex(int betIndex, RandomNumberGenerator rng);

    List<String> getStrip();
}
