package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SixOfEighteenGame implements BonusGame {
    private final SixOfEighteenConfig config;

    public SixOfEighteenGame(SixOfEighteenConfig config) {
        this.config = config;
    }

    @Override
    public BonusGameResult play(RandomNumberGenerator rng) {
        List<Integer> positions = new ArrayList<>(config.getPositions());
        for (int i = positions.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            Integer temp = positions.get(i);
            positions.set(i, positions.get(j));
            positions.set(j, temp);
        }
        int winners = Math.min(config.getWinnerCount(), positions.size());
        List<WinEntry> winData = new ArrayList<>();
        int total = 0;
        for (int i = 0; i < winners; i++) {
            int win = positions.get(i);
            winData.add(new WinEntry("Position " + (i + 1), win));
            total += win;
        }
        return new BonusGameResult("SIX_OF_EIGHTEEN", winData, total);
    }
}
