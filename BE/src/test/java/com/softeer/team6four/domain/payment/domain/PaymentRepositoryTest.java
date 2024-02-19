package com.softeer.team6four.domain.payment.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softeer.team6four.domain.common.RepositoryTest;

@RepositoryTest
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
	}
}
