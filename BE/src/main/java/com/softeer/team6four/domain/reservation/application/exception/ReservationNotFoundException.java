package com.softeer.team6four.domain.reservation.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class ReservationNotFoundException extends ReservationException {
	public ReservationNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

}
