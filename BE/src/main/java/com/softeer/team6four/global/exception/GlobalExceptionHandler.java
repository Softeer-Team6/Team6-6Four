package com.softeer.team6four.global.exception;

import com.softeer.team6four.domain.payment.application.exception.PaymentException;
import com.softeer.team6four.domain.reservation.application.exception.ReservationException;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.carbob.application.exception.CarbobException;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 추가한 커스텀 예외 처리를 정의하면 Handler 를 추가 작성
    @ExceptionHandler(ReservationException.class)
    public ErrorResponseDto handleReservationException(ReservationException e) {
        log.warn("ReservationException : {}", e.getMessage());
        return ErrorResponseDto.map(e.getErrorCode());
    }

    @ExceptionHandler(PaymentException.class)
    public ErrorResponseDto handlePaymentException(PaymentException e) {
        log.warn("PaymentException : {}", e.getMessage());
        return ErrorResponseDto.map(e.getErrorCode());
    }

    @ExceptionHandler(UserException.class)
    public ErrorResponseDto handleUserException(UserException e) {
        log.warn("UserException : {}", e.getMessage());
        return ErrorResponseDto.map(e.getErrorCode());
    }

    @ExceptionHandler(CarbobException.class)
    public ErrorResponseDto handleCarbobException(CarbobException e) {
        log.warn("CarbobException : {}", e.getMessage());
        return ErrorResponseDto.map(e.getErrorCode());
    }

    @ExceptionHandler(BusinessException.class)
    public ErrorResponseDto handleBusinessException(BusinessException e) {
        log.warn("BusinessException : {}", e.getMessage());
        return ErrorResponseDto.map(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ErrorResponseDto handleException(Exception e) {
        log.error("Exception : {}", e.getMessage());
        log.error("Exception : {}", e.getClass());
        return ErrorResponseDto.map(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
