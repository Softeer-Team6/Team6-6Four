package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyCarbobSummary {
	private final @NotNull Long carbobId;
	private final @NotNull String nickname;
	private final @NotNull String address;
	private final @NotNull String imageUrl;
	private final @NotNull Long reservationCount;
}
