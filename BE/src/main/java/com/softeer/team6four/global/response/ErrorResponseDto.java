package com.softeer.team6four.global.response;

import lombok.Getter;

@Getter
public class ErrorResponseDto extends ResponseDto {
    private ErrorResponseDto(ErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getMessage(), null);
    }

    private ErrorResponseDto(ErrorCode errorCode, String message) {
        super(errorCode.getErrorCode(), errorCode.getMessage(), message);
    }

    public static ErrorResponseDto map(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto map(ErrorCode errorCode, String message) {
        return new ErrorResponseDto(errorCode, message);
    }
}
