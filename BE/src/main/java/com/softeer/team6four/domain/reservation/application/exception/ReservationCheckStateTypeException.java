package com.softeer.team6four.domain.reservation.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class ReservationCheckStateTypeException extends ReservationException {
	public ReservationCheckStateTypeException(ErrorCode errorCode) {
		super(errorCode);
	}
}
