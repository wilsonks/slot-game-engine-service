package com.slotcentral.gameengine.rng;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomNumberGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    public int nextReelPos() {
        return secureRandom.nextInt(128) + 1;
    }

    public int nextInt(int bound) {
        return secureRandom.nextInt(bound);
    }

    public int nextIntRange(int min, int max) {
        return min + secureRandom.nextInt(max - min + 1);
    }
}
