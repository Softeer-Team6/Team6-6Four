package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.carbob.domain.SpeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class AroundCarbobListInfoSummary {
    private final @NotNull Long carbobId;
    private final @NotNull String feePerHour;
    private final @NotNull Double latitude;
    private final @NotNull Double longitude;

    public AroundCarbobListInfoSummary
        (
             Long carbobId, Integer feePerHour,
             Object latitude, Object longitude
        )
    {
        this.carbobId = carbobId;
        this.feePerHour = feePerHour+"원/kwh";
        this.latitude = (Double) latitude;
        this.longitude = (Double) longitude;
    }
}
