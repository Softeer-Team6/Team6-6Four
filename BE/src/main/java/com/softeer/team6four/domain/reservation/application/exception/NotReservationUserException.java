package com.softeer.team6four.domain.reservation.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class NotReservationUserException extends ReservationException {
    public NotReservationUserException(ErrorCode errorCode) {
        super(errorCode);
    }

}
