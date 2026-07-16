package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OneOfThreeGame implements BonusGame {
    private final OneOfThreeConfig config;

    public OneOfThreeGame(OneOfThreeConfig config) {
        this.config = config;
    }

    @Override
    public BonusGameResult play(RandomNumberGenerator rng) {
        int pick = rng.nextInt(config.getOptions().size());
        int win = config.getOptions().get(pick);
        return new BonusGameResult("ONE_OF_THREE", List.of(new WinEntry("Option " + pick, win)), win);
    }
}
