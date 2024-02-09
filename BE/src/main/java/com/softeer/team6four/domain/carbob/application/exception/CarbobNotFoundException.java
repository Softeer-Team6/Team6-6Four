package com.softeer.team6four.domain.carbob.application.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class CarbobNotFoundException extends CarbobException {
    public CarbobNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
