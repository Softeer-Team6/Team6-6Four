package com.softeer.team6four.domain.reservation.application.exception;

import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;

public class ReservationException extends BusinessException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
