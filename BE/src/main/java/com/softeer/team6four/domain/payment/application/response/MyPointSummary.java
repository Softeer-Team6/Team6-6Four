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
    private final @NotNull Long targetId;
    private final @NotNull String pointTitle;
}
