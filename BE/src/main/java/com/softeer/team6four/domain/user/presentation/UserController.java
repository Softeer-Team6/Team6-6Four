package com.softeer.team6four.domain.user.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	private final UserJoinService userJoinService;

	@GetMapping(value = "/auth/email/check")
	public ResponseDto<EmailCheck> checkEmail(@RequestParam String email) {
		EmailCheck emailCheck = userJoinService.findEmail(email);
		return ResponseDto.map(HttpStatus.OK.value(), "사용 가능한 이메일 입니다.", emailCheck);
	}

	@GetMapping(value = "/auth/nickname/check")
	public ResponseDto<NicknameCheck> checkNickname(@RequestParam String nickname) {
		NicknameCheck nicknameCheck = userJoinService.findNickname(nickname);
		return ResponseDto.map(HttpStatus.OK.value(), "사용 가능한 닉네임 입니다.", nicknameCheck);
	}

	@PostMapping("/auth/signup")
	public ResponseDto<Void> signup(@RequestBody SignUpRequest signupRequest) {
		userJoinService.signup(signupRequest);
		return ResponseDto.map(HttpStatus.OK.value(), "회원가입에 성공했습니다.", null);
	}

	@PostMapping("/auth/signin")
	public ResponseDto<SignInResponse> login(@RequestBody SignInRequest signinRequest) {
		SignInResponse signInResponse = userJoinService.signin(signinRequest);
		return ResponseDto.map(HttpStatus.OK.value(), "로그인에 성공했습니다.", signInResponse);
	}

}

