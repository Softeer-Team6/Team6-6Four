package com.softeer.team6four.domain.user.application.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
	private final String accessToken;
	private final String refreshToken;

	@Builder
	public SignInResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
