package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.application.response.NicknameCheck;
import com.softeer.team6four.domain.user.application.response.SignInResponse;
import com.softeer.team6four.domain.user.application.request.SignInRequest;
import com.softeer.team6four.domain.user.application.request.SignUpRequest;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.auth.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserJoinService 테스트")
class UserJoinServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtProvider jwtProvider;

	@InjectMocks
	private UserJoinService userJoinService;

	private String email;
	private String nickname;

	private User expectedUser;

	@BeforeEach
	void setUp() {
		email = "test@exmple.com";
		nickname = "test";
		ReflectionTestUtils.setField(userJoinService, "jwtProvider", jwtProvider);
		expectedUser = User.builder()
			.email("test@example.com")
			.nickname("nickname")
			.password("password")
			.build();
		ReflectionTestUtils.setField(expectedUser, "userId", 1L);
	}

	@Test
	@DisplayName("이메일 존재 여부 확인 - 이메일이 존재하는 경우")
	void findEmail_EmailExists() {
		// When
		when(userRepository.existsByEmail(email)).thenReturn(true);

		// Then
		assertThrows(UserException.class, () -> userJoinService.findEmail(email));
	}

	@Test
	@DisplayName("이메일 존재 여부 확인 - 이메일이 존재하지 않는 경우")
	void findEmail_EmailNotExists() {
		// Given
		when(userRepository.existsByEmail(email)).thenReturn(false);

		// When
		EmailCheck emailCheck = userJoinService.findEmail(email);

		// Then
		assertFalse(emailCheck.isEmailExists());
		assertDoesNotThrow(() -> userJoinService.findEmail(email));
	}

	@Test
	@DisplayName("닉네임 존재 여부 확인 - 닉네임이 존재하는 경우")
	void findNickname_NicknameExists() {
		// When
		when(userRepository.existsByNickname(nickname)).thenReturn(true);

		// Then
		UserException exception = assertThrows(UserException.class, () -> userJoinService.findNickname(nickname));
		assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
	}

	@Test
	@DisplayName("닉네임 존재 여부 확인 - 닉네임이 존재하지 않는 경우")
	void findNickname_NicknameNotExists() {
		// Given
		when(userRepository.existsByNickname(nickname)).thenReturn(false);

		// When
		NicknameCheck nicknameCheck = userJoinService.findNickname(nickname);

		// Then
		assertFalse(nicknameCheck.isNicknameExists());
		assertDoesNotThrow(() -> userJoinService.findNickname(nickname));
	}

	@Test
	@DisplayName("사용자 가입 - 가입에 성공하는 경우")
	void signup_Success() {
		// Given
		SignUpRequest signUpRequest = SignUpRequest.create("test@example.com", "password","test");
		User savedUser = expectedUser;
		when(userRepository.save(any())).thenReturn(savedUser);

		// When
		Void result = userJoinService.signup(signUpRequest);

		// Then
		assertNotNull(result);
		assertEquals(expectedUser, result);
	}


	@Test
	@DisplayName("사용자 로그인 - 로그인에 성공하는 경우")
	void signin_Success() {
		// Given
		SignInRequest signInRequest = SignInRequest.create("test@example.com", "password");

		User user = expectedUser;
		when(userRepository.findUserByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
		when(jwtProvider.generateAccessToken(1L)).thenReturn("access_token");
		when(jwtProvider.generateRefreshToken(1L)).thenReturn("refresh_token");

		// When
		SignInResponse result = userJoinService.signin(signInRequest);

		// Then
		assertNotNull(result);
		assertEquals("nickname", result.getNickname());
		assertEquals("access_token", result.getAccessToken());
		assertEquals("refresh_token", result.getRefreshToken());
	}


	@Test
	@DisplayName("사용자 로그인 - 사용자를 찾을 수 없는 경우")
	void signin_UserNotFound() {
		// Given
		SignInRequest signInRequest = SignInRequest.create("test@example.com", "password");

		// When
		when(userRepository.findUserByEmail("test@example.com")).thenReturn(java.util.Optional.empty());

		// Then
		assertThrows(UserException.class, () -> userJoinService.signin(signInRequest));
	}

	@Test
	@DisplayName("사용자 로그인 - 비밀번호가 올바르지 않은 경우")
	void signin_InvalidPassword() {
		// Given
		SignInRequest signInRequest = SignInRequest.create("test@example.com", "invalid_password");

		User user = User.builder()
						.email("test@example.com")
						.password("password")
						.nickname("nickname")
						.build();

		//When
		when(userRepository.findUserByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

		// Then
		assertThrows(UserException.class, () -> userJoinService.signin(signInRequest));
	}


}
