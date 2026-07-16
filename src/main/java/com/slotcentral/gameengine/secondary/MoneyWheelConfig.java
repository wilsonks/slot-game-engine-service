package com.slotcentral.gameengine.secondary;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "money-wheel")
public class MoneyWheelConfig {
    private List<Segment> segments;

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public static class Segment {
        private int weight;
        private int winAmount;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getWinAmount() {
            return winAmount;
        }

        public void setWinAmount(int winAmount) {
            this.winAmount = winAmount;
        }
    }
}
