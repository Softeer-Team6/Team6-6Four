package com.softeer.team6four.domain.payment.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softeer.team6four.domain.payment.application.PaymentPointService;
import com.softeer.team6four.domain.payment.application.request.ChargeRequest;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentPointService paymentPointService;

	@Auth
	@GetMapping(value = "/total/point")
	public ResponseDto<TotalPoint> getMyPointTotal() {
		Long userId = UserContextHolder.get();

		TotalPoint point = paymentPointService.calculateTotalPoint(userId);

		return ResponseDto.map(HttpStatus.OK.value(), "포인트 총액(충전+수입+지출)입니다", point);
	}

	@Auth
	@GetMapping(value = "/my/point")
	public ResponseDto<SliceResponse<MyPointSummary>> getMyPointList
		(
			@RequestParam(required = false) Long lastPaymentId,
			@PageableDefault(size = 6) Pageable pageable
		) {
		Long userId = UserContextHolder.get();
		SliceResponse<MyPointSummary> myPointSummaryList = paymentPointService.getMyPointSummaryList(userId,
			lastPaymentId, pageable);

		return ResponseDto.map(HttpStatus.OK.value(), "내포인트 조회에 성공했습니다.", myPointSummaryList);
	}

	@Auth
	@PostMapping(value = "/charge")
	public ResponseDto<Void> ChargeMyPoint(@RequestBody ChargeRequest chargeRequest) {
		Long userId = UserContextHolder.get();
		paymentPointService.registMyPoint(userId, chargeRequest);

		return ResponseDto.map(HttpStatus.OK.value(), "포인트 충전되었습니다", null);
	}

}

