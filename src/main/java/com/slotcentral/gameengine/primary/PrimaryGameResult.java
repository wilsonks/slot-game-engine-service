package com.slotcentral.gameengine.primary;

import java.util.List;

public record PrimaryGameResult(
        int winAmount,
        List<Integer> cyclicWinArr,
        boolean anticipationFlag,
        boolean jackpot1Win,
        boolean jackpot2Win,
        boolean jackpot3Win,
        boolean jackpot4Win
) {
}
