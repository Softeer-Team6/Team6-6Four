package com.softeer.team6four.domain.reservation.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class InvalidReservationTimeLinesException extends ReservationException {
    public InvalidReservationTimeLinesException(ErrorCode errorCode) {
        super(errorCode);
    }

}
