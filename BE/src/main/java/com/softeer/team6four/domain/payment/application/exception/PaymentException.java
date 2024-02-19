package com.softeer.team6four.domain.payment.application.exception;

import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;

public class PaymentException extends BusinessException {
	public PaymentException(ErrorCode errorCode) {
		super(errorCode);
	}
}
