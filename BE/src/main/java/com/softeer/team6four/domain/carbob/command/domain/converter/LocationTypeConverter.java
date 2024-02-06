package com.softeer.team6four.domain.carbob.command.domain.converter;

import com.softeer.team6four.domain.carbob.command.domain.LocationType;
import com.softeer.team6four.global.infrastructure.database.EnumConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocationTypeConverter extends EnumConverter<LocationType> {
    public LocationTypeConverter() {
        super(LocationType.class);
    }
}
