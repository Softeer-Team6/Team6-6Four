package com.softeer.team6four.domain.payment.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.payment.domain.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyPointSummary {
    private final @NotNull Long paymentId;
    private final @NotNull Integer amount;
    private final @NotNull LocalDateTime createdDate;
    private final @NotNull PayType paymentType;
    private @NotNull String pointTitle;
    private final Long targetId;

    public MyPointSummary
            (
                    Long paymentId, Integer amount, LocalDateTime createdDate,
                    PayType paymentType, Long targetId
            )
    {
        this.paymentId = paymentId;
        this.amount = amount;
        this.createdDate = createdDate;
        this.paymentType = paymentType;
        this.targetId = targetId;
    }

    public void setPointTitleByPaymentType(String title){
        this.pointTitle = title;
    }

}
