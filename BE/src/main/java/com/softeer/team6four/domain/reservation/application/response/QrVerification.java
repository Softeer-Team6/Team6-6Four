package com.softeer.team6four.domain.reservation.application.response;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QrVerification {
	private final @NotNull Long reservationId;
	private final @NotNull Boolean isVerified;
}
