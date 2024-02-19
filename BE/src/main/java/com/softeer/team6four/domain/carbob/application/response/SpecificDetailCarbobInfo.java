package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.carbob.domain.ChargerType;
import com.softeer.team6four.domain.carbob.domain.InstallType;
import com.softeer.team6four.domain.carbob.domain.LocationType;
import com.softeer.team6four.domain.carbob.domain.SpeedType;

import lombok.Getter;

@Getter
public class SpecificDetailCarbobInfo {
	private final @NotNull Long carbobId;
	private final @NotNull String nickname;
	private final @NotNull String imageUrl;
	private final @NotNull Double distance;
	private final @NotNull String address;
	private final @NotNull String feePerHour;
	private final @NotNull String chargerType;
	private final @NotNull String speedType;
	private final @NotNull String installType;
	private final @NotNull String description;

	public SpecificDetailCarbobInfo
		(
			Long carbobId, String nickname, String imageUrl,
			Object distance, String address, Integer feePerHour,
			ChargerType chargerType, SpeedType speedType,
			InstallType installType, LocationType locationType,
			String description
		) {
		this.carbobId = carbobId;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.distance = Math.floor((Double)distance / 100) / 10.0;
		;
		this.address = address;
		this.feePerHour = feePerHour + "원/kwh";
		;
		this.chargerType = chargerType.getValue();
		this.speedType = speedType.getValue() + "kwh";
		this.installType = installType.getValue() + "/" + locationType.getValue();
		this.description = description;
	}
}

