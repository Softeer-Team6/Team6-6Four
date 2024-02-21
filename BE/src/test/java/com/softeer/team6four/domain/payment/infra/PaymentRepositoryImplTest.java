package com.softeer.team6four.domain.payment.infra;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.softeer.team6four.domain.common.RepositoryTest;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;

@RepositoryTest
class PaymentRepositoryImplTest {

	@Autowired
	private PaymentRepositoryImpl paymentRepositoryImpl;
	private Long userId;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		userId = 1L;
		pageable = Pageable.ofSize(6);
	}

	@Test
	@DisplayName("내 포인트 조회 - 처음 요청")
	void 내_포인트_조회_처음_요청() {
		// given
		Long lastPaymentId = null;

		// when
		Slice<MyPointSummary> actualSlice = paymentRepositoryImpl.findMyPointSummaryList(userId, lastPaymentId,
			pageable);

		// then
		assertEquals(6, actualSlice.getContent().size()); // 페이지 크기만큼의 결과가 반환되는지 확인
	}

	@Test
	@DisplayName("내 포인트 조회 - 다음 페이지 요청(마지막)")
	void 내_포인트_조회_다음_페이지_요청() {
		// given
		Long lastPaymentId = 6L;

		// when
		Slice<MyPointSummary> actualSlice = paymentRepositoryImpl.findMyPointSummaryList(userId, lastPaymentId,
			pageable);

		// then
		assertEquals(false, actualSlice.hasNext()); // 다음 페이지가 없는지 확인
	}

}
