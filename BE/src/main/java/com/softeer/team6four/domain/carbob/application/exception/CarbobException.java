package com.softeer.team6four.domain.carbob.application.exception;

import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;

public class CarbobException extends BusinessException {
    public CarbobException(ErrorCode errorCode) {
        super(errorCode);
    }
}
