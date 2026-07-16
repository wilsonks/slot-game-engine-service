package com.slotcentral.gameengine.primary;

import com.slotcentral.gameengine.paytable.PaytableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PrimaryGameEngineTest {
    @Autowired
    private PrimaryGameEngine engine;

    @Autowired
    private PaytableService paytableService;

    @Test
    void sevenSevenSeven_maxBet_jackpot1And2() {
        PrimaryGameResult r = engine.compute("SEVEN", "SEVEN", "SEVEN", 4, 4, paytableService);
        assertThat(r.jackpot1Win()).isTrue();
        assertThat(r.jackpot2Win()).isTrue();
        assertThat(r.winAmount()).isGreaterThan(0);
    }

    @Test
    void bar3Bar3Bar3_correctWin() {
        PrimaryGameResult r = engine.compute("BAR3", "BAR3", "BAR3", 0, 4, paytableService);
        int betAmount = paytableService.getBetAmount(0, 4);
        assertThat(r.winAmount()).isEqualTo(500 * betAmount);
    }

    @Test
    void cherry_first_smallWin() {
        PrimaryGameResult r = engine.compute("CHERRY", "BLANK", "BLANK", 0, 4, paytableService);
        assertThat(r.winAmount()).isGreaterThan(0);
    }

    @Test
    void blank_blank_blank_noWin() {
        PrimaryGameResult r = engine.compute("BLANK", "BLANK", "BLANK", 0, 4, paytableService);
        assertThat(r.winAmount()).isEqualTo(0);
    }

    @Test
    void anticipation_sevenSeven() {
        PrimaryGameResult r = engine.compute("SEVEN", "SEVEN", "BLANK", 0, 4, paytableService);
        assertThat(r.anticipationFlag()).isTrue();
    }

    @Test
    void mixedBar_wins() {
        PrimaryGameResult r = engine.compute("BAR", "BAR2", "BAR3", 0, 4, paytableService);
        assertThat(r.winAmount()).isGreaterThan(0);
    }
}
