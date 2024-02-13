package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.carbob.domain.ChargerType;
import com.softeer.team6four.domain.carbob.domain.SpeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AroundCarbobListInfo {
    private final @NotNull Long carbobId;
    private final @NotNull String nickname;
    private final @NotNull String address;
    private final @NotNull Integer feePerHour;
    private final @NotNull SpeedType speedType;
}

