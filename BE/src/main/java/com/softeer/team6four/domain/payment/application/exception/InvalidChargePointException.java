package com.softeer.team6four.domain.payment.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class InvalidChargePointException extends PaymentException{
    public InvalidChargePointException(ErrorCode errorCode) {
        super(errorCode);
    }
}

