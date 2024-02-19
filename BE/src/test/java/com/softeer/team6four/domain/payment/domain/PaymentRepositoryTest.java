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
	void findByUser_UserId() {
		//given
		//Payment 객체들을 저장할 리스트 생성
		List<Payment> paymentList = new ArrayList<>();

		Payment payment1 = Payment.builder()
			.amount(1000)
			.payType(PayType.CHARGE)
			.targetId(0L)
			.user(user)
			.build();
		paymentList.add(payment1);
		paymentRepository.save(payment1);

		Payment payment2 = Payment.builder()
			.amount(2000)
			.payType(PayType.CHARGE)
			.targetId(1L)
			.user(user)
			.build();
		paymentList.add(payment2);
		paymentRepository.save(payment2);

		//when
		List<Payment> savedPayment = paymentRepository.findByUser_UserId(user.getUserId());

		//then
		assertEquals(savedPayment, paymentList);
		assertEquals(savedPayment.size(), paymentList.size());
	}

	@Test
	void findPaymentsByPayTypeAndTargetIdIn() {
	}
}
