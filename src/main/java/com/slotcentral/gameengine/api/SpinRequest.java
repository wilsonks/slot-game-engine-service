package com.slotcentral.gameengine.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SpinRequest(
        @NotBlank String spinId,
        @NotBlank String gameId,
        @NotNull @Min(0) @Max(4) Integer betIndex,
        @NotNull @Min(0) @Max(8) Integer denomIndex,
        String egmId,
        ReelStopOverrides reelStopOverrides,
        boolean includeTrace
) {
}
