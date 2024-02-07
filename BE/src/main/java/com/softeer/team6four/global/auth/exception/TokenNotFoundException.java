package com.softeer.team6four.global.auth.exception;

import com.softeer.team6four.global.response.ErrorCode;

public class TokenNotFoundException extends JwtException{
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
