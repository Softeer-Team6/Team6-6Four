package com.softeer.team6four.domain.carbob.domain.converter;

import com.softeer.team6four.domain.carbob.domain.SpeedType;
import com.softeer.team6four.global.infrastructure.database.EnumConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SpeedTypeConverter extends EnumConverter<SpeedType> {
    public SpeedTypeConverter() {
        super(SpeedType.class);
    }
}
