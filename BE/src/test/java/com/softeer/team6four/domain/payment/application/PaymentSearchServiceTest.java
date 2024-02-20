package com.softeer.team6four.domain.payment.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.softeer.team6four.domain.payment.domain.PayType;
import com.softeer.team6four.domain.payment.domain.Payment;
import com.softeer.team6four.domain.payment.domain.PaymentRepository;
import com.softeer.team6four.domain.reservation.application.ReservationSearchService;

@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentSearchServiceTest 테스트")
class PaymentSearchServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private ReservationSearchService reservationSearchService;

	@InjectMocks
	private PaymentSearchService paymentSearchService;

	@Test
	@DisplayName("카밥의 총소득 조회 성공")
	void getTotalIncomeByTargetId_ReturnsSumOfPayments() {
		// given
		Long carbobId = 1L;
		List<Long> usedReservationIds = Arrays.asList(1L, 2L);
		List<Payment> payments = Arrays.asList(
			Payment.builder().amount(100).payType(PayType.INCOME).targetId(1L).build(),
			Payment.builder().amount(200).payType(PayType.INCOME).targetId(2L).build()
		);

		when(reservationSearchService.getUsedReservationIdListByCarbobId(carbobId)).thenReturn(usedReservationIds);
		when(paymentRepository.findPaymentsByPayTypeAndTargetIdIn(PayType.INCOME, usedReservationIds)).thenReturn(payments);

		// when
		Long totalIncome = paymentSearchService.getTotalIncomeByTargetId(carbobId);

		// then
		assertEquals(300L, totalIncome);

		verify(reservationSearchService, times(1)).getUsedReservationIdListByCarbobId(carbobId);
		verify(paymentRepository, times(1)).findPaymentsByPayTypeAndTargetIdIn(PayType.INCOME, usedReservationIds);
	}
}
