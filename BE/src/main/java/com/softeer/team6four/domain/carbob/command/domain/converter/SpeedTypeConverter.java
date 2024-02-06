package com.softeer.team6four.domain.carbob.command.domain.converter;

import com.softeer.team6four.domain.carbob.command.domain.SpeedType;
import com.softeer.team6four.global.infrastructure.database.EnumConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SpeedTypeConverter extends EnumConverter<SpeedType> {
    public SpeedTypeConverter() {
        super(SpeedType.class);
    }
}
