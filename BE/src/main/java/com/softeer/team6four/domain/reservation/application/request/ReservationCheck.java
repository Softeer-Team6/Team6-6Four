package com.softeer.team6four.domain.reservation.application.request;

import com.softeer.team6four.domain.reservation.domain.StateType;

import lombok.Getter;

@Getter
public class ReservationCheck {
	private StateType stateType;

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
}

