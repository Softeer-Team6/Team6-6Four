package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyCarbobDetailInfo {
	private final @NotNull Long carbobId;

	private final @NotNull String nickname;
	private final @NotNull String imageUrl;
	private final @NotNull String qrImageUrl;
	private final @NotNull String address;
	private final @NotNull String feePerHour;

	private final @NotNull String chargerType;
	private final @NotNull String speedType;
	private final @NotNull String installType;

	private final @NotNull String description;
	private final @NotNull String selfUseTime;

	private final @NotNull Long carbobTotalIncome;
}
