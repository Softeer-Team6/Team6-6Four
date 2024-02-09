package com.softeer.team6four.domain.payment.application.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ChargeRequest {
    private Integer chargePoint;
}
