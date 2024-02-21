package com.softeer.team6four.domain.reservation.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.reservation.application.ReservationTime;
import com.softeer.team6four.domain.reservation.domain.StateType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationInfo {
	private final @NotNull Long reservationId;
	private final @NotNull String carbobImageUrl;
	private final @NotNull StateType stateType;
	private final @NotNull ReservationTime reservationTime;
	private final @NotNull Integer totalFee;
	private final @NotNull String carbobNickname;
	private final @NotNull String address;
	private @NotNull String reservationTimeStr;

	public void convertReservationTimeToStr() {
		this.reservationTimeStr = this.reservationTime.toString();
	}
}
