package com.slotcentral.gameengine.secondary;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "six-of-eighteen")
public class SixOfEighteenConfig {
    private List<Integer> positions;
    private int winnerCount = 3;

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public int getWinnerCount() {
        return winnerCount;
    }

    public void setWinnerCount(int winnerCount) {
        this.winnerCount = winnerCount;
    }
}
