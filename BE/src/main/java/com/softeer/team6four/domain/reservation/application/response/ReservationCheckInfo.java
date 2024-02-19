package com.softeer.team6four.domain.reservation.application.response;

import com.softeer.team6four.domain.reservation.domain.StateType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationCheckInfo {
	private final String stateType;

	@Builder
	ReservationCheckInfo(StateType stateType) {
		this.stateType = stateType.getValue();
	}
}

