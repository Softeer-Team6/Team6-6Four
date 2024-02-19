package com.softeer.team6four.domain.reservation.infra;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCheckEvent {
	private final User host;
	private final User guest;
	private final Carbob carbob;
	private final String stateType;
}
