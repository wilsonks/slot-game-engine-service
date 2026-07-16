package com.slotcentral.gameengine.secondary;

import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecondaryGameEngine {
    private final MoneyWheelGame moneyWheelGame;
    private final HitOrMissGame hitOrMissGame;
    private final OneOfThreeGame oneOfThreeGame;
    private final SixOfEighteenGame sixOfEighteenGame;

    public SecondaryGameEngine(MoneyWheelGame moneyWheelGame, HitOrMissGame hitOrMissGame,
                               OneOfThreeGame oneOfThreeGame, SixOfEighteenGame sixOfEighteenGame) {
        this.moneyWheelGame = moneyWheelGame;
        this.hitOrMissGame = hitOrMissGame;
        this.oneOfThreeGame = oneOfThreeGame;
        this.sixOfEighteenGame = sixOfEighteenGame;
    }

    public BonusGameResult getSecondaryWin(String s1, String s2, String s3,
                                           int betIndex, int denomIndex,
                                           RandomNumberGenerator rng) {
        if ("BONUS".equals(s1) && "BONUS".equals(s2) && "BONUS".equals(s3)) {
            return sixOfEighteenGame.play(rng);
        } else if ("BONUS".equals(s1) && "BONUS".equals(s2)) {
            return oneOfThreeGame.play(rng);
        } else if ("BONUS".equals(s1)) {
            return hitOrMissGame.play(rng);
        } else if ("BELL".equals(s1) && "BELL".equals(s2) && "BELL".equals(s3)) {
            return moneyWheelGame.play(rng);
        }
        return new BonusGameResult("NONE", List.of(), 0);
    }
}
