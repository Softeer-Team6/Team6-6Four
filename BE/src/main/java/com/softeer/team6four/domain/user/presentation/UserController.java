package com.softeer.team6four.domain.user.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softeer.team6four.domain.user.application.EmailService;
import com.softeer.team6four.domain.user.application.NicknameService;
import com.softeer.team6four.domain.user.application.UserJoinService;
import com.softeer.team6four.domain.user.application.request.SignInRequest;
import com.softeer.team6four.domain.user.application.request.SignUpRequest;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.application.response.NicknameCheck;
import com.softeer.team6four.domain.user.application.response.SignInResponse;
import com.softeer.team6four.global.response.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final EmailService emailService;
	private final NicknameService nicknameService;
	private final UserJoinService userJoinService;

	@GetMapping(value = "/auth/email/check")
	public ResponseDto<EmailCheck> checkEmail(@RequestParam String email) {
		return emailService.findEmail(email);
	}

	@GetMapping(value = "/auth/nickname/check")
	public ResponseDto<NicknameCheck> checkNickname(@RequestParam String nickname) {
		return nicknameService.findNickname(nickname);
	}

	@PostMapping("/auth/signup")
	public void signup(@RequestBody SignUpRequest signupRequest) {
		userJoinService.signup(signupRequest);
	}

	@PostMapping("/auth/signin")
	public ResponseDto<SignInResponse> login(@RequestBody SignInRequest signinRequest) {
		return userJoinService.signin(signinRequest);
	}

}

