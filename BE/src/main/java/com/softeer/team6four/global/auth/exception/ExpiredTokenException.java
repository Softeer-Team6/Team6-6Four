package com.softeer.team6four.global.auth.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class ExpiredTokenException extends JwtException{
    public ExpiredTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
