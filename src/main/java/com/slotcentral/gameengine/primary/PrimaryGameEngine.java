package com.slotcentral.gameengine.primary;

import com.slotcentral.gameengine.paytable.PaytableService;
import com.slotcentral.gameengine.paytable.SymbolPayoutTable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class PrimaryGameEngine {
    private static final Set<String> BAR_SYMBOLS = Set.of("BAR", "BAR2", "BAR3");

    private final SymbolPayoutTable symbolPayoutTable;

    public PrimaryGameEngine(SymbolPayoutTable symbolPayoutTable) {
        this.symbolPayoutTable = symbolPayoutTable;
    }

    public PrimaryGameResult compute(String s1, String s2, String s3,
                                     int betIndex, int denomIndex,
                                     PaytableService paytableService) {
        int betAmount = paytableService.getBetAmount(betIndex, denomIndex);
        int multiplier = 0;
        List<Integer> cyclicWinArr = new ArrayList<>();
        boolean anticipationFlag = ("SEVEN".equals(s1) && "SEVEN".equals(s2))
                || ("BONUS".equals(s1) && "BONUS".equals(s2));

        String key = s1 + "|" + s2 + "|" + s3;

        if (symbolPayoutTable.getPayouts().containsKey(key)) {
            multiplier = symbolPayoutTable.getPayouts().get(key);
            cyclicWinArr = List.of(0, 1, 2);
        } else if (BAR_SYMBOLS.contains(s1) && BAR_SYMBOLS.contains(s2) && BAR_SYMBOLS.contains(s3)) {
            multiplier = 50;
            cyclicWinArr = List.of(0, 1, 2);
        } else if ("CHERRY".equals(s1) && "CHERRY".equals(s2)) {
            multiplier = 10;
            cyclicWinArr = List.of(0, 1);
        } else if ("CHERRY".equals(s1)) {
            multiplier = 2;
            cyclicWinArr = List.of(0);
        }

        int winAmount = multiplier * betAmount;

        boolean jackpot1Win = "SEVEN".equals(s1) && "SEVEN".equals(s2) && "SEVEN".equals(s3) && betIndex == 4;
        boolean jackpot2Win = "SEVEN".equals(s1) && "SEVEN".equals(s2) && "SEVEN".equals(s3);
        boolean jackpot3Win = "BAR3".equals(s1) && "BAR3".equals(s2) && "BAR3".equals(s3) && betIndex == 4;
        boolean jackpot4Win = "BONUS".equals(s1) && "BONUS".equals(s2) && "BONUS".equals(s3);

        return new PrimaryGameResult(winAmount, cyclicWinArr, anticipationFlag,
                jackpot1Win, jackpot2Win, jackpot3Win, jackpot4Win);
    }
}
