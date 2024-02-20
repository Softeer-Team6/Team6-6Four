package com.softeer.team6four.domain.reservation.application.request;

import com.softeer.team6four.domain.reservation.domain.StateType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReservationCheck {
	private StateType stateType;
}

