package com.softeer.team6four.global.auth.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class InvalidTokenException extends JwtException {
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}

