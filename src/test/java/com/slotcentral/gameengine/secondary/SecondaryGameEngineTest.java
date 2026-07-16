package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecondaryGameEngineTest {
    @Autowired
    private SecondaryGameEngine engine;

    @Autowired
    private RandomNumberGenerator rng;

    @Test
    void bonusBonusBonus_triggersSixOfEighteen() {
        BonusGameResult r = engine.getSecondaryWin("BONUS", "BONUS", "BONUS", 0, 4, rng);
        assertThat(r.gameType()).isEqualTo("SIX_OF_EIGHTEEN");
    }

    @Test
    void bonusBonusCherry_triggersOneOfThree() {
        BonusGameResult r = engine.getSecondaryWin("BONUS", "BONUS", "CHERRY", 0, 4, rng);
        assertThat(r.gameType()).isEqualTo("ONE_OF_THREE");
    }

    @Test
    void bellBellBell_triggersMoneyWheel() {
        BonusGameResult r = engine.getSecondaryWin("BELL", "BELL", "BELL", 0, 4, rng);
        assertThat(r.gameType()).isEqualTo("MONEY_WHEEL");
    }

    @Test
    void bonusOnly_triggersHitOrMiss() {
        BonusGameResult r = engine.getSecondaryWin("BONUS", "CHERRY", "BLANK", 0, 4, rng);
        assertThat(r.gameType()).isEqualTo("HIT_OR_MISS");
    }

    @Test
    void noBonus_returnsNone() {
        BonusGameResult r = engine.getSecondaryWin("BLANK", "BLANK", "BLANK", 0, 4, rng);
        assertThat(r.gameType()).isEqualTo("NONE");
    }
}
