package com.softeer.team6four.domain.payment.domain;

import com.softeer.team6four.global.infrastructure.database.EnumConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PayTypeConverter extends EnumConverter<PayType> {
    public PayTypeConverter() {
        super(PayType.class);
    }
}
