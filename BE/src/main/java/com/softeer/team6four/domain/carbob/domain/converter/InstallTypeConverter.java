package com.softeer.team6four.domain.carbob.domain.converter;

import com.softeer.team6four.domain.carbob.domain.InstallType;
import com.softeer.team6four.global.infrastructure.database.EnumConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InstallTypeConverter extends EnumConverter<InstallType> {
    public InstallTypeConverter() {
        super(InstallType.class);
    }
}
