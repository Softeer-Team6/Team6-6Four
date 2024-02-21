package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserSearchService 테스트")
public class UserSearchServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserSearchService userSearchService;

	private User expectedUser;

	@BeforeEach
	void setUp() {
		expectedUser = User.builder()
			.email("test1@test.com")
			.nickname("user1")
			.password("1234")
			.build();
		ReflectionTestUtils.setField(expectedUser, "userId", 1L);
	}

	@Test
	@DisplayName("userId로 유저 찾기 테스트 - 성공")
	public void userId로_유저찾기_테스트_성공() {
		// given
		when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

		// when
		User actualUser = userSearchService.findUserByUserId(1L);

		// then
		assertNotNull(actualUser);
		assertEquals(expectedUser.getUserId(), actualUser.getUserId());
		assertEquals(expectedUser.getNickname(), actualUser.getNickname());

		verify(userRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("userId로 유저 찾기 테스트 - 실패")
	public void userId로_유저찾기_테스트_실패() {
		// given
		when(userRepository.findById(2L)).thenReturn(Optional.empty());

		// when
		assertThrows(UserException.class, () -> {
			userSearchService.findUserByUserId(2L);
		});

		// then
		verify(userRepository, times(1)).findById(2L);
	}
}
