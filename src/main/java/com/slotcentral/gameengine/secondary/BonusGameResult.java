package com.slotcentral.gameengine.secondary;

import java.util.List;

public record BonusGameResult(String gameType, List<WinEntry> winData, int totalWin) {
}
