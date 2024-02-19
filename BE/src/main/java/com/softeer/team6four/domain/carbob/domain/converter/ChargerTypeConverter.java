package com.softeer.team6four.domain.carbob.domain.converter;

import com.softeer.team6four.domain.carbob.domain.ChargerType;
import com.softeer.team6four.global.infrastructure.database.EnumConverter;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ChargerTypeConverter extends EnumConverter<ChargerType> {
	public ChargerTypeConverter() {
		super(ChargerType.class);
	}
}
