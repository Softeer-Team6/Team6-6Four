package com.softeer.team6four.domain.payment.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.softeer.team6four.domain.payment.application.exception.InvalidChargePointException;
import com.softeer.team6four.domain.payment.application.request.ChargeRequest;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.domain.payment.domain.PayType;
import com.softeer.team6four.domain.payment.domain.Payment;
import com.softeer.team6four.domain.payment.domain.PaymentRepository;
import com.softeer.team6four.domain.payment.infra.PaymentRepositoryImpl;
import com.softeer.team6four.domain.user.application.UserSearchService;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentPointService {
	private final PaymentRepository paymentRepository;
	private final UserSearchService userSearchService;
	private final PaymentRepositoryImpl paymentRepositoryImpl;

	public TotalPoint calculateTotalPoint(Long userId) {

		int totalPoint = paymentRepository.sumAmountByUserId(userId);

		return TotalPoint.builder()
			.totalPoint(totalPoint)
			.build();
	}

	public SliceResponse<MyPointSummary> getMyPointSummaryList
		(Long userId, Long lastPaymentId, Pageable pageable) {

		Slice<MyPointSummary> myPointSummaryList = paymentRepositoryImpl.findMyPointSummaryList(userId, lastPaymentId,
			pageable);
		return SliceResponse.of(myPointSummaryList);
	}

	public void registMyPoint(Long userId, ChargeRequest chargeRequest) {

		User user = userSearchService.findUserByUserId(userId);

		Integer inputPoint = chargeRequest.getChargePoint();

		if (inputPoint <= 0) {
			throw new InvalidChargePointException(ErrorCode.INVALID_CHARGE_NEGATIVE);
		}

		Payment payment = Payment.builder()
			.amount(inputPoint)
			.payType(PayType.CHARGE)
			.targetId(0L)
			.user(user)
			.build();
		paymentRepository.save(payment);
	}

}

