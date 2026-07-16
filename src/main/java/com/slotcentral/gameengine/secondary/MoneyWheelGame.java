package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MoneyWheelGame implements BonusGame {
    private final MoneyWheelConfig config;

    public MoneyWheelGame(MoneyWheelConfig config) {
        this.config = config;
    }

    @Override
    public BonusGameResult play(RandomNumberGenerator rng) {
        int totalWeight = config.getSegments().stream()
                .mapToInt(MoneyWheelConfig.Segment::getWeight)
                .sum();
        int pick = rng.nextInt(totalWeight);
        int cumulative = 0;
        MoneyWheelConfig.Segment selected = config.getSegments().get(config.getSegments().size() - 1);
        for (MoneyWheelConfig.Segment seg : config.getSegments()) {
            cumulative += seg.getWeight();
            if (pick < cumulative) {
                selected = seg;
                break;
            }
        }
        List<WinEntry> wins = List.of(new WinEntry("Money Wheel Win", selected.getWinAmount()));
        return new BonusGameResult("MONEY_WHEEL", wins, selected.getWinAmount());
    }
}
