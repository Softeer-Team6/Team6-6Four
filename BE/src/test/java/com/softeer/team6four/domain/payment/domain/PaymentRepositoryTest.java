package com.softeer.team6four.domain.payment.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.softeer.team6four.domain.common.RepositoryTest;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;

@RepositoryTest
class PaymentRepositoryTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private UserRepository userRepository;
	private User user;

	@BeforeEach
	void setUp() {
		user = User.builder()
			.email("test1@test.com")
			.nickname("user1")
			.password("1234")
			.build();
		ReflectionTestUtils.setField(user, "userId", 1L);
	}

	@Test
	@DisplayName("UserId로 payment 조회")
	void UserId로_payment조회() {
		//given
		int expectedSize = 8;

		//when
		List<Payment> savedPayment = paymentRepository.findByUser_UserId(user.getUserId());

		//then
		assertEquals(savedPayment.size(), expectedSize);
	}

	@Test
	@DisplayName("특정 유저의 포인트 총합 계산 - Payment에 해당 유저의 정보 있을 때")
	void 특정_유저의_포인트총합게산() {
		//given
		int expectedAmount = 8000;

		//when
		int actualAmount = paymentRepository.sumAmountByUserId(user.getUserId());

		//then
		assertEquals(expectedAmount, actualAmount);
	}

	@Test
	@DisplayName("특정 유저의 포인트 총합 계산- Payment에 유저 정보가 없을 때")
	void 특정_유저의_포인트총합게산_정보없을때() {
		//given
		int expectedAmount = 0;

		//when
		int actualAmount = paymentRepository.sumAmountByUserId(10L);

		//then
		assertEquals(expectedAmount, actualAmount);
	}

}
