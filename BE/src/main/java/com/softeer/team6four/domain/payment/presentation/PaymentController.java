package com.softeer.team6four.domain.payment.presentation;

import com.softeer.team6four.domain.payment.application.PaymentPointService;
import com.softeer.team6four.domain.payment.application.request.ChargeRequest;
import com.softeer.team6four.domain.payment.application.response.ChargePoint;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
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

    @GetMapping(value = "/total/point")
    public ResponseDto<TotalPoint> getMyPointTotal() {
        // TODO : UserContextHold 에서 userId 가져와야함
        Long userId = 1L;
        return paymentPointService.calculateTotalPoint(userId);
    }

    @GetMapping(value = "/my/point")
    public ResponseDto<SliceResponse<MyPointSummary>> getMyReservationApplicationList
            (
                    @RequestParam(required = false) Long lastPaymentId,
                    @PageableDefault(size = 6) Pageable pageable
            )
    {
        // TODO : UserContextHold 에서 userId 가져오기
        Long userId = 1L;
        return paymentPointService.getMypointSummaryList(userId, lastPaymentId, pageable);
    }

    @PostMapping(value = "/charge")
    public ResponseDto<ChargePoint> ChargeMyPoint(@RequestBody ChargeRequest chargeRequest) {

        // TODO : UserContextHold 에서 userId 가져와야함
        Long userId = 1L;
        return paymentPointService.registMyPoint(userId, chargeRequest);
    }

}

