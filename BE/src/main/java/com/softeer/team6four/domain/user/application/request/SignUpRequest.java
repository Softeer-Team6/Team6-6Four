package com.softeer.team6four.domain.user.application.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
	private String email;
	private String password;
	private String nickname;

	private SignUpRequest(String email, String password,String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}

	public static SignUpRequest create(String email, String password, String nickname) {
		return new SignUpRequest(email, password,nickname);
	}
}
