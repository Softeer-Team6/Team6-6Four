package com.softeer.team6four.domain.payment.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.domain.payment.domain.PayType;
import com.softeer.team6four.domain.payment.domain.Payment;
import com.softeer.team6four.domain.payment.domain.PaymentRepository;
import com.softeer.team6four.domain.user.domain.User;

@ExtendWith(MockitoExtension.class)
class PaymentPointServiceTest {

	@Mock
	private PaymentRepository paymentRepository;
	@InjectMocks
	private PaymentPointService paymentPointService;

	@BeforeEach
	void setUp() {

	}


	@Test
	@DisplayName("총 포인트 조회하기")
	void 총_포인트_조회하기() {
		// given
		Long userId = 1L;
		int expectedTotalPoint = 100 - 50 + 200;

		// paymentRepository.sumAmountByUserId(userId)가 호출될 때 반환할 값을 설정
		when(paymentRepository.sumAmountByUserId(userId)).thenReturn(expectedTotalPoint);

		// when
		TotalPoint totalPoint = paymentPointService.calculateTotalPoint(userId);

		// then
		assertEquals(expectedTotalPoint, totalPoint.getTotalPoint());
	}


	@Test
	void getMypointSummaryList() {

	}

	@Test
	void registMyPoint() {
	}
}
