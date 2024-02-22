package com.softeer.team6four.domain.notification.application;

import com.softeer.team6four.domain.notification.application.request.FcmTokenRequest;
import com.softeer.team6four.domain.notification.domain.FcmToken;
import com.softeer.team6four.domain.notification.domain.FcmTokenRepository;
import com.softeer.team6four.domain.user.application.UserSearchService;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.response.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FcmService 테스트")
class FcmServiceTest {
	@Mock
	private FcmTokenRepository fcmTokenRepository;

	@Mock
	private UserSearchService userSearchService;

	@InjectMocks
	private FcmService fcmService;

	private User expectedUser;

	@BeforeEach
	void setUp() {
		expectedUser = User.builder()
			.email("test@example.com")
			.nickname("nickname")
			.password("password")
			.build();
		ReflectionTestUtils.setField(expectedUser, "userId", 1L);
	}

	@Test
	@DisplayName("FCM 토큰 저장 성공 테스트")
	void saveToken_Success() {
		// Given
		FcmTokenRequest fcmTokenRequest = FcmTokenRequest.of("testFCMToken");

		when(userSearchService.findUserByUserId(1L)).thenReturn(expectedUser);

		// When
		fcmService.saveToken(1L, fcmTokenRequest);

		// Then
		verify(userSearchService, times(1)).findUserByUserId(1L);
		verify(fcmTokenRepository, times(1)).save(any(FcmToken.class));
	}

	@Test
	@DisplayName("FCM 토큰 저장 실패 테스트")
	void saveToken_Failure() {
		// Given
		FcmTokenRequest fcmTokenRequest = FcmTokenRequest.of("testFCMToken");
		when(userSearchService.findUserByUserId(1L)).thenReturn(expectedUser);

		doThrow(new RuntimeException("FCM 토큰 저장 실패")).when(fcmTokenRepository).save(any(FcmToken.class));

		// When & Then
		assertThrows(RuntimeException.class, () -> fcmService.saveToken(1L, fcmTokenRequest));
		verify(userSearchService, times(1)).findUserByUserId(1L);
		verify(fcmTokenRepository, times(1)).save(any(FcmToken.class));
	}

	@Test
	@DisplayName("유효하지 않은 사용자 ID에 대한 FCM 토큰 저장 테스트")
	void saveToken_InvalidUserId() {
		// Given
		FcmTokenRequest fcmTokenRequest = FcmTokenRequest.of("testFCMToken");

		when(userSearchService.findUserByUserId(2L)).thenThrow(new UserException(ErrorCode.USER_NOT_FOUND));

		// When & Then
		assertThrows(UserException.class, () -> fcmService.saveToken(2L, fcmTokenRequest));
		verify(userSearchService, times(1)).findUserByUserId(2L);
		verifyNoInteractions(fcmTokenRepository);
	}


}
