package com.slotcentral.gameengine.api;

import com.slotcentral.gameengine.secondary.BonusGameResult;
import lombok.Data;

import java.util.List;

@Data
public class SpinResponse {
    private String spinId;
    private String gameId;
    private String egmId;
    private int betIndex;
    private int denomIndex;
    private int betAmount;
    private int reel1Stop;
    private int reel2Stop;
    private int reel3Stop;
    private String symbol1;
    private String symbol2;
    private String symbol3;
    private int primaryWinAmount;
    private List<Integer> cyclicWinArr;
    private boolean anticipationFlag;
    private boolean jackpot1Win;
    private boolean jackpot2Win;
    private boolean jackpot3Win;
    private boolean jackpot4Win;
    private double jackpotContribution;
    private BonusGameResult bonusGameResult;
    private int totalWinAmount;
    private List<Integer> spinTrace;
}
