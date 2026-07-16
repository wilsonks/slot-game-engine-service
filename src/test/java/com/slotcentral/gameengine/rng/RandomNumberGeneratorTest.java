package com.slotcentral.gameengine.rng;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RandomNumberGeneratorTest {
    private final RandomNumberGenerator rng = new RandomNumberGenerator();

    @Test
    void nextReelPos_returnsInRange() {
        for (int i = 0; i < 1000; i++) {
            int pos = rng.nextReelPos();
            assertThat(pos).isBetween(1, 128);
        }
    }

    @Test
    void nextInt_returnsInRange() {
        for (int i = 0; i < 1000; i++) {
            int val = rng.nextInt(10);
            assertThat(val).isBetween(0, 9);
        }
    }

    @Test
    void nextIntRange_returnsInRange() {
        for (int i = 0; i < 1000; i++) {
            int val = rng.nextIntRange(5, 15);
            assertThat(val).isBetween(5, 15);
        }
    }
}
