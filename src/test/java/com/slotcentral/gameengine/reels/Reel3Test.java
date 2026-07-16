package com.slotcentral.gameengine.reels;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Reel3Test {
    @Autowired
    private Reel3 reel3;

    @Test
    void stripHas128Symbols() {
        assertThat(reel3.getStrip()).hasSize(128);
    }

    @Test
    void getSymbolAtIndex1() {
        assertThat(reel3.getSymbol(1)).isNotNull().isNotBlank();
    }

    @Test
    void getSymbolAtIndex128() {
        assertThat(reel3.getSymbol(128)).isNotNull().isNotBlank();
    }

    @Test
    void noNullSymbols() {
        reel3.getStrip().forEach(s -> assertThat(s).isNotNull().isNotBlank());
    }
}
