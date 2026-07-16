package com.slotcentral.gameengine.paytable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaytableConfigTest {
    @Autowired
    private PaytableConfig paytableConfig;

    @Autowired
    private PaytableService paytableService;

    @Test
    void betValuesMatrixIs5x9() {
        assertThat(paytableConfig.getBetValues()).hasSize(5);
        paytableConfig.getBetValues().forEach(row -> assertThat(row).hasSize(9));
    }

    @Test
    void defaultBetDenomIndices() {
        assertThat(paytableConfig.getDefaultBetIndex()).isEqualTo(0);
        assertThat(paytableConfig.getDefaultDenomIndex()).isEqualTo(4);
    }

    @Test
    void getBetAmountFirstBetFifthDenom() {
        assertThat(paytableService.getBetAmount(0, 4)).isEqualTo(20);
    }
}
