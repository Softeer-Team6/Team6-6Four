package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.carbob.domain.ChargerType;
import com.softeer.team6four.domain.carbob.domain.SpeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AroundCarbobListInfo {
    private final @NotNull Long carbobId;
    private final @NotNull String nickname;
    private final @NotNull String address;
    private final @NotNull String feePerHour;
    private final @NotNull String speedType;


    public AroundCarbobListInfo
        (
            Long carbobId, String nickname, String address,
            Integer feePerHour, SpeedType speedType
        )
    {
        this.carbobId = carbobId;
        this.nickname = nickname;
        this.address = address;
        this.feePerHour = feePerHour+"원/kwh";
        this.speedType = speedType.getValue()+"kwh";
    }
}

