package com.softeer.team6four.domain.carbob.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class NotCarbobOwnerException extends CarbobException {
	public NotCarbobOwnerException(ErrorCode errorCode) {
		super(errorCode);
	}

}
