package com.softeer.team6four.domain.payment.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softeer.team6four.domain.common.RepositoryTest;

@RepositoryTest
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;

@DataJpaTest
class PaymentRepositoryTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	@DisplayName("User_UserId로 Payment 조회")
	void findByUser_UserId() {
		// given
		Long userId = 1L;

		// when
		List<Payment> payments = paymentRepository.findByUser_UserId(userId);

		// then
		assertNotNull(payments);
		assertEquals(1, payments.size());
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
		userRepository.save(user);
	}

	@Test
	@DisplayName("UserId로 payment 조회")
	void UserId로_payment조화() {
		//given
		List<Payment> expectedPayments = createMockPayments();

		//when
		List<Payment> savedPayment = paymentRepository.findByUser_UserId(user.getUserId());

		//then
		assertEquals(savedPayment, expectedPayments);
		assertEquals(savedPayment.size(), expectedPayments.size());
	}

	@Test
	@DisplayName("특정 유저의 포인트 총합 계산 - Payment에 해당 유저의 정보 있을 때")
	void 특정_유저의_포인트총합게산() {
		//given
		int expectedAmount = createMockAmount();

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
		int actualAmount = paymentRepository.sumAmountByUserId(user.getUserId());

		//then
		assertEquals(expectedAmount, actualAmount);
	}

	@Test
	void findPaymentsByPayTypeAndTargetIdIn() {
	}

	private int createMockAmount() {
		// 가상의 Payment 객체 리스트 생성
		int amount1 = 1000;
		Payment payment1 = Payment.builder().amount(amount1).payType(PayType.CHARGE).targetId(0L).user(user)
			.build();
		paymentRepository.save(payment1);

		int amount2 = 2000;
		Payment payment2 = Payment.builder().amount(amount2).payType(PayType.CHARGE).targetId(1L).user(user)
			.build();
		paymentRepository.save(payment2);

		return payment1.getAmount() + payment2.getAmount();
	}

	private List<Payment> createMockPayments() {
		// 가상의 Payment 객체 리스트 생성
		List<Payment> payments = new ArrayList<>();
		Payment payment1 = Payment.builder().amount(1000).payType(PayType.CHARGE).targetId(0L).user(user)
			.build();
		payments.add(payment1);
		paymentRepository.save(payment1);

		Payment payment2 = Payment.builder().amount(2000).payType(PayType.CHARGE).targetId(1L).user(user)
			.build();
		payments.add(payment2);
		paymentRepository.save(payment2);
		return payments;
	}
}
