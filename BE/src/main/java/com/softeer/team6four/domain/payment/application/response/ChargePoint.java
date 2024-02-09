package com.softeer.team6four.domain.payment.application.response;

import com.google.firebase.database.annotations.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePoint {
    private final Integer point;

    @Builder
    public ChargePoint(Integer point){
        this.point = point;
    }
}

