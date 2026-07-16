package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HitOrMissGame implements BonusGame {
    private final HitOrMissConfig config;

    public HitOrMissGame(HitOrMissConfig config) {
        this.config = config;
    }

    @Override
    public BonusGameResult play(RandomNumberGenerator rng) {
        double roll = (double) rng.nextInt(10000) / 10000.0;
        if (roll < config.getHitProbability()) {
            int win = config.getBaseWin() * config.getMultiplier();
            return new BonusGameResult("HIT_OR_MISS", List.of(new WinEntry("HIT", win)), win);
        }
        return new BonusGameResult("HIT_OR_MISS", List.of(new WinEntry("MISS", 0)), 0);
    }
}
