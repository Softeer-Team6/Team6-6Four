package com.softeer.team6four.global.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> {
	private int statusCode;
	private String message;
	private LocalDateTime timestamp;
	private T data;

	@Builder
	public ResponseDto(int statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.message = message;
		this.timestamp = LocalDateTime.now();
		this.data = data;
	}

	public static <T> ResponseDto<T> map(int statusCode, String message, T data) {
		return ResponseDto.<T>builder()
			.statusCode(statusCode)
			.message(message)
			.data(data)
			.build();
	}
}
