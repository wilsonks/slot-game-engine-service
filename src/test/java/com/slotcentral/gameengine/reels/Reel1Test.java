package com.slotcentral.gameengine.reels;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Reel1Test {
    @Autowired
    private Reel1 reel1;

    @Test
    void stripHas128Symbols() {
        assertThat(reel1.getStrip()).hasSize(128);
    }

    @Test
    void getSymbolAtIndex1() {
        assertThat(reel1.getSymbol(1)).isNotNull().isNotBlank();
    }

    @Test
    void getSymbolAtIndex128() {
        assertThat(reel1.getSymbol(128)).isNotNull().isNotBlank();
    }

    @Test
    void noNullSymbols() {
        reel1.getStrip().forEach(s -> assertThat(s).isNotNull().isNotBlank());
    }
}
