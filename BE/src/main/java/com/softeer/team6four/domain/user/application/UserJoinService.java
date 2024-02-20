package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.application.request.SignInRequest;
import com.softeer.team6four.domain.user.application.request.SignUpRequest;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.application.response.NicknameCheck;
import com.softeer.team6four.domain.user.application.response.SignInResponse;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.auth.JwtProvider;
import com.softeer.team6four.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Void signup(SignUpRequest signupRequest) {
		User newUser = User.builder()
			.email(signupRequest.getEmail())
			.password(signupRequest.getPassword())
			.nickname(signupRequest.getNickname())
			.build();
		userRepository.save(newUser);
		return null;
	}

	/**
	 * 사용자 로그인을 처리합니다.
	 *
	 * @param signInRequest 로그인 요청 객체
	 * @return 로그인에 성공한 경우 사용자 정보를 담은 SignInResponse 객체를 반환합니다.
	 * @throws UserException 이메일에 해당하는 사용자를 찾을 수 없는 경우 발생합니다.
	 * @throws UserException 비밀번호가 올바르지 않은 경우 발생합니다.
	 */
	@Transactional(readOnly = true)
	public SignInResponse signin(SignInRequest signInRequest) {
		String email = signInRequest.getEmail();
		String password = signInRequest.getPassword();

		User user = userRepository.findUserByEmail(email)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

		if (!user.getPassword().equals(password)) {
			throw new UserException(ErrorCode.INVALID_PASSWORD);
		}

		String nickname = user.getNickname();
		String accessToken = jwtProvider.generateAccessToken(user.getUserId());
		String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

		return SignInResponse.builder()
			.nickname(nickname)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}


}
