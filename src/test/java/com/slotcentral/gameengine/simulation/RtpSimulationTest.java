package com.slotcentral.gameengine.simulation;

import com.slotcentral.gameengine.api.SpinRequest;
import com.slotcentral.gameengine.api.SpinResponse;
import com.slotcentral.gameengine.api.SpinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RtpSimulationTest {
    @Autowired
    private SpinService spinService;

    @Test
    void rtpWithin75To99Percent() {
        int spins = 100_000;
        long totalBet = 0;
        long totalWin = 0;
        for (int i = 0; i < spins; i++) {
            SpinRequest req = new SpinRequest("sim-" + i, "GAME001", 2, 4, null, null, false);
            SpinResponse resp = spinService.computeSpin(req);
            totalBet += resp.getBetAmount();
            totalWin += resp.getTotalWinAmount();
        }
        double rtp = (double) totalWin / totalBet;
        assertThat(rtp).isBetween(0.75, 0.99);
    }
}
