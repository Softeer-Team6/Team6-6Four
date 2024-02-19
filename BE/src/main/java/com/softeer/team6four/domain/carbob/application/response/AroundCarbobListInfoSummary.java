package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;

import lombok.Getter;

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
		) {
		this.carbobId = carbobId;
		this.feePerHour = feePerHour + "원/kwh";
		this.latitude = (Double)latitude;
		this.longitude = (Double)longitude;
	}
}
