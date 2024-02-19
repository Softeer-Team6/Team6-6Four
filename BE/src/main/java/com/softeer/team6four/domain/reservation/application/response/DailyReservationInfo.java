package com.softeer.team6four.domain.reservation.application.response;

import lombok.Getter;

@Getter
public class DailyReservationInfo {
	boolean[] dailyBookedTimeCheck = new boolean[24];

	public DailyReservationInfo(boolean[] dailyBookedTimeCheck) {
		this.dailyBookedTimeCheck = dailyBookedTimeCheck;
	}

}
