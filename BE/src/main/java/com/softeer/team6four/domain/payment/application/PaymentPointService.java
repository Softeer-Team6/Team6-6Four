package com.softeer.team6four.domain.payment.application;
import com.softeer.team6four.domain.payment.application.response.TotalPoint;
import com.softeer.team6four.domain.payment.domain.Payment;
import com.softeer.team6four.domain.payment.domain.PaymentRepository;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentPointService {
    private final PaymentRepository paymentRepository;
    public ResponseDto<TotalPoint> calculateTotalPoint(Long userId){
        List<Payment> payments = paymentRepository.findByUser_UserId(userId);

        int totalPoint = 0;
        for (Payment payment : payments) {
            totalPoint += payment.getAmount();
        }

        TotalPoint point = TotalPoint.builder()
                .totalPoint(totalPoint)
                .build();

        return ResponseDto.map(HttpStatus.OK.value(), "포인트 총액(충전+수입+지출)입니다", point);
    }

}
