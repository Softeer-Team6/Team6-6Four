package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AroundCarbobListInfoSummary {
    private final @NotNull Long carbobId;
    private final @NotNull Integer feePerHour;
    private final @NotNull Double latitude;
    private final @NotNull Double longitude;
    private final @NotNull Double distance;

}
