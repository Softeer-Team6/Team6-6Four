package com.softeer.team6four.domain.reservation.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class NotApproveReservationException extends ReservationException {
	public NotApproveReservationException(ErrorCode errorCode) {
		super(errorCode);
	}

}
