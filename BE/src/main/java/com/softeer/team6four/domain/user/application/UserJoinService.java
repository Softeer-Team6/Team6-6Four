package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.application.response.NicknameCheck;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.user.application.exception.UserNotFoundException;
import com.softeer.team6four.domain.user.application.request.SignInRequest;
import com.softeer.team6four.domain.user.application.request.SignUpRequest;
import com.softeer.team6four.domain.user.application.response.SignInResponse;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.auth.JwtProvider;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserJoinService {
	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public EmailCheck findEmail(String email) {
			boolean emailExists = userRepository.existsByEmail(email);
			EmailCheck emailCheck = new EmailCheck(emailExists);

			if (emailExists) {
				throw new UserException(ErrorCode.EMAIL_DUPLICATE);
			}
			return new EmailCheck(emailExists);
	}

	@Transactional(readOnly = true)
	public NicknameCheck findNickname(String nickname) {
		boolean nicknameExists = userRepository.existsByNickname(nickname);
		NicknameCheck nicknameCheck = new NicknameCheck(nicknameExists);

		if (nicknameExists) {
			throw new UserException(ErrorCode.NICKNAME_DUPLICATE);
		}
		return new NicknameCheck(nicknameExists);
	}


	@Transactional
	public void signup(SignUpRequest signupRequest) {
		User newUser = User.builder()
			.email(signupRequest.getEmail())
			.password(signupRequest.getPassword())
			.nickname(signupRequest.getNickname())
			.build();
		userRepository.save(newUser);

	}

	@Transactional(readOnly = true)
	public ResponseDto<SignInResponse> signin(SignInRequest signInRequest) {
		User user = userRepository.findUserByEmail(signInRequest.getEmail())
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

		String nickname = user.getNickname();
		String accessToken = jwtProvider.generateAccessToken(user.getUserId());
		String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

		SignInResponse signInResponse = SignInResponse.builder()
			.nickname(nickname)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
		return ResponseDto.map(HttpStatus.OK.value(), "로그인에 성공했습니다.", signInResponse);

	}

}
