package com.softeer.team6four.domain.user.application.exception;

import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;

public class EmailDuplicateException extends UserException {
    public EmailDuplicateException(ErrorCode errorCode) {super(errorCode);}
}
