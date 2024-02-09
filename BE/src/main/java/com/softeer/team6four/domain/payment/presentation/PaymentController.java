package com.softeer.team6four.domain.payment.presentation;

import com.softeer.team6four.domain.payment.application.PaymentPointService;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentPointService paymentPointService;

    @GetMapping(value = "/total-point")
    public ResponseDto<TotalPoint> getMyPointTotal()
    {
        // TODO : UserContextHold 에서 userId 가져와야함
        Long userId = 1L;
        return paymentPointService.calculateTotalPoint(userId);
    }

}
