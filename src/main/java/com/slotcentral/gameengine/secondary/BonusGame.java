package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;

public interface BonusGame {
    BonusGameResult play(RandomNumberGenerator rng);
}
