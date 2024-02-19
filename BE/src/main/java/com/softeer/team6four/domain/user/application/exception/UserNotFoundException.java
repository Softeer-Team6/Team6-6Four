package com.softeer.team6four.domain.user.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class UserNotFoundException extends UserException {

	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
