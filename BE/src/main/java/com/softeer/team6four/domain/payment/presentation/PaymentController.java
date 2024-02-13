package com.softeer.team6four.domain.payment.presentation;

import com.softeer.team6four.domain.payment.application.PaymentPointService;
import com.softeer.team6four.domain.payment.application.request.ChargeRequest;
import com.softeer.team6four.domain.payment.application.response.ChargePoint;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentPointService paymentPointService;

    @Auth
    @GetMapping(value = "/total/point")
    public ResponseDto<TotalPoint> getMyPointTotal() {
        Long userId = UserContextHolder.get();
        return paymentPointService.calculateTotalPoint(userId);
    }

    @Auth
    @GetMapping(value = "/my/point")
    public ResponseDto<SliceResponse<MyPointSummary>> getMyPointList
            (
                    @RequestParam(required = false) Long lastPaymentId,
                    @PageableDefault(size = 6) Pageable pageable
            )
    {
        Long userId = UserContextHolder.get();
        return paymentPointService.getMypointSummaryList(userId, lastPaymentId, pageable);
    }

    @Auth
    @PostMapping(value = "/charge")
    public ResponseDto<ChargePoint> ChargeMyPoint(@RequestBody ChargeRequest chargeRequest) {
        Long userId = UserContextHolder.get();
        return paymentPointService.registMyPoint(userId, chargeRequest);
    }

}

