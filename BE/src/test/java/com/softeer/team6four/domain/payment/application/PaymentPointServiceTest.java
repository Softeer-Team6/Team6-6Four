package com.softeer.team6four.domain.payment.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.softeer.team6four.domain.payment.application.exception.InvalidChargePointException;
import com.softeer.team6four.domain.payment.application.request.ChargeRequest;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.domain.payment.domain.Payment;
import com.softeer.team6four.domain.payment.domain.PaymentRepository;
import com.softeer.team6four.domain.user.application.UserSearchService;
import com.softeer.team6four.domain.user.domain.User;

@ExtendWith(MockitoExtension.class)
class PaymentPointServiceTest {

	@Mock
	private PaymentRepository paymentRepository;
	@InjectMocks
	private PaymentPointService paymentPointService;
	@Mock
	private UserSearchService userSearchService;
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
	@DisplayName("내 포인트 조회하기 - 처음 요청시")
	void 내_포인트_조회하기() {

	}

	@Test
	@DisplayName("내 포인트 등록 - 정상입력(양수)")
	void 포인트_정상_입력_테스트() {
		//given
		ChargeRequest chargeRequest = ChargeRequest.builder().chargePoint(1000).build();
		when(userSearchService.findUserByUserId(user.getUserId())).thenReturn(user);

		//when
		paymentPointService.registMyPoint(user.getUserId() ,chargeRequest);

		// then
		verify(paymentRepository).save(any(Payment.class));
	}
	@Test
	@DisplayName("내 포인트 등록 - 비정상입력(음수)")
	void 포인트_비정상_입력_테스트() {
		//given
		ChargeRequest chargeRequest = ChargeRequest.builder().chargePoint(-1000).build();

		//when, then
		assertThrows(InvalidChargePointException.class, () -> {
			paymentPointService.registMyPoint(user.getUserId(), chargeRequest);
		});
	}

}
