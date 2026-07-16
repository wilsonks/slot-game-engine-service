package com.slotcentral.gameengine.reels;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Reel2Test {
    @Autowired
    private Reel2 reel2;

    @Test
    void stripHas128Symbols() {
        assertThat(reel2.getStrip()).hasSize(128);
    }

    @Test
    void getSymbolAtIndex1() {
        assertThat(reel2.getSymbol(1)).isNotNull().isNotBlank();
    }

    @Test
    void getSymbolAtIndex128() {
        assertThat(reel2.getSymbol(128)).isNotNull().isNotBlank();
    }

    @Test
    void noNullSymbols() {
        reel2.getStrip().forEach(s -> assertThat(s).isNotNull().isNotBlank());
    }
}
