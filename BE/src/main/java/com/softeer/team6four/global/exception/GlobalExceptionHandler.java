package com.softeer.team6four.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.softeer.team6four.domain.carbob.application.exception.CarbobException;
import com.softeer.team6four.domain.payment.application.exception.PaymentException;
import com.softeer.team6four.domain.reservation.application.exception.ReservationException;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ErrorResponseDto;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 추가한 커스텀 예외 처리를 정의하면 Handler 를 추가 작성
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponseDto>handleMissingParams(MissingServletRequestParameterException e) {
		log.error("MissingServletRequestParameterException : {}", e.getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(ErrorCode.MISSING_REQUEST_PARAMETER));
	}

	@ExceptionHandler(ReservationException.class)
	public ResponseEntity<ErrorResponseDto>handleReservationException(ReservationException e) {
		log.error("ReservationException : {}", e.getErrorCode().getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(e.getErrorCode()));
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorResponseDto>handlePaymentException(PaymentException e) {
		log.error("PaymentException : {}", e.getErrorCode().getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(e.getErrorCode()));
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponseDto> handleUserException(UserException e) {
		log.error("UserException : {}", e.getErrorCode().getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(e.getErrorCode()));
	}

	@ExceptionHandler(CarbobException.class)
	public ResponseEntity<ErrorResponseDto>handleCarbobException(CarbobException e) {
		log.error("CarbobException : {}", e.getErrorCode().getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(e.getErrorCode()));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponseDto>handleBusinessException(BusinessException e) {
		log.error("BusinessException : {}", e.getErrorCode().getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(e.getErrorCode()));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponseDto>handleException(Exception e) {
		log.error("Exception : {}", e.getMessage());
		Sentry.captureException(e);
		return ResponseEntity.badRequest().body(ErrorResponseDto.map(ErrorCode.INTERNAL_SERVER_ERROR));
	}
}
