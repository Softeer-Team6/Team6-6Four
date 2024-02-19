package com.softeer.team6four.domain.user.application.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {
	private String email;
	private String password;

	private SignInRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public static SignInRequest create(String email, String password) {
		return new SignInRequest(email, password);
	}
}

