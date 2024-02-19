package com.softeer.team6four.domain.reservation.domain;

import com.softeer.team6four.global.infrastructure.database.EnumConverter;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StateTypeConverter extends EnumConverter<StateType> {
	public StateTypeConverter() {
		super(StateType.class);
	}
}
