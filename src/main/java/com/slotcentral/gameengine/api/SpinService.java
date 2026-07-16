package com.slotcentral.gameengine.api;

import com.slotcentral.gameengine.paytable.PaytableService;
import com.slotcentral.gameengine.primary.PrimaryGameEngine;
import com.slotcentral.gameengine.primary.PrimaryGameResult;
import com.slotcentral.gameengine.reels.Reel1;
import com.slotcentral.gameengine.reels.Reel2;
import com.slotcentral.gameengine.reels.Reel3;
import com.slotcentral.gameengine.rng.RandomNumberGenerator;
import com.slotcentral.gameengine.secondary.BonusGameResult;
import com.slotcentral.gameengine.secondary.SecondaryGameEngine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpinService {
    private final RandomNumberGenerator rng;
    private final Reel1 reel1;
    private final Reel2 reel2;
    private final Reel3 reel3;
    private final PaytableService paytableService;
    private final PrimaryGameEngine primaryGameEngine;
    private final SecondaryGameEngine secondaryGameEngine;
    private final GaffingProperties gaffingProperties;

    public SpinService(RandomNumberGenerator rng, Reel1 reel1, Reel2 reel2, Reel3 reel3,
                       PaytableService paytableService, PrimaryGameEngine primaryGameEngine,
                       SecondaryGameEngine secondaryGameEngine, GaffingProperties gaffingProperties) {
        this.rng = rng;
        this.reel1 = reel1;
        this.reel2 = reel2;
        this.reel3 = reel3;
        this.paytableService = paytableService;
        this.primaryGameEngine = primaryGameEngine;
        this.secondaryGameEngine = secondaryGameEngine;
        this.gaffingProperties = gaffingProperties;
    }

    public SpinResponse computeSpin(SpinRequest request) {
        List<Integer> trace = request.includeTrace() ? new ArrayList<>() : null;

        int stop1;
        int stop2;
        int stop3;
        if (gaffingProperties.isEnabled() && request.reelStopOverrides() != null) {
            stop1 = request.reelStopOverrides().s1() != null
                    ? request.reelStopOverrides().s1()
                    : reel1.getStopIndex(request.betIndex(), rng);
            stop2 = request.reelStopOverrides().s2() != null
                    ? request.reelStopOverrides().s2()
                    : reel2.getStopIndex(request.betIndex(), rng);
            stop3 = request.reelStopOverrides().s3() != null
                    ? request.reelStopOverrides().s3()
                    : reel3.getStopIndex(request.betIndex(), rng);
        } else {
            stop1 = reel1.getStopIndex(request.betIndex(), rng);
            stop2 = reel2.getStopIndex(request.betIndex(), rng);
            stop3 = reel3.getStopIndex(request.betIndex(), rng);
        }

        if (trace != null) {
            trace.add(stop1);
            trace.add(stop2);
            trace.add(stop3);
        }

        String s1 = reel1.getSymbol(stop1);
        String s2 = reel2.getSymbol(stop2);
        String s3 = reel3.getSymbol(stop3);

        PrimaryGameResult primary = primaryGameEngine.compute(
                s1, s2, s3, request.betIndex(), request.denomIndex(), paytableService);
        BonusGameResult bonus = secondaryGameEngine.getSecondaryWin(
                s1, s2, s3, request.betIndex(), request.denomIndex(), rng);

        int betAmount = paytableService.getBetAmount(request.betIndex(), request.denomIndex());
        double jackpotContribution = paytableService.getJackpotContribution(
                request.betIndex(), request.denomIndex());
        int totalWin = primary.winAmount() + bonus.totalWin();

        SpinResponse resp = new SpinResponse();
        resp.setSpinId(request.spinId());
        resp.setGameId(request.gameId());
        resp.setEgmId(request.egmId());
        resp.setBetIndex(request.betIndex());
        resp.setDenomIndex(request.denomIndex());
        resp.setBetAmount(betAmount);
        resp.setReel1Stop(stop1);
        resp.setReel2Stop(stop2);
        resp.setReel3Stop(stop3);
        resp.setSymbol1(s1);
        resp.setSymbol2(s2);
        resp.setSymbol3(s3);
        resp.setPrimaryWinAmount(primary.winAmount());
        resp.setCyclicWinArr(primary.cyclicWinArr());
        resp.setAnticipationFlag(primary.anticipationFlag());
        resp.setJackpot1Win(primary.jackpot1Win());
        resp.setJackpot2Win(primary.jackpot2Win());
        resp.setJackpot3Win(primary.jackpot3Win());
        resp.setJackpot4Win(primary.jackpot4Win());
        resp.setJackpotContribution(jackpotContribution);
        resp.setBonusGameResult(bonus);
        resp.setTotalWinAmount(totalWin);
        resp.setSpinTrace(trace);
        return resp;
    }
}
