package com.softeer.team6four.domain.carbob.domain;

import com.softeer.team6four.global.infrastructure.database.CodeValue;

public enum InstallType implements CodeValue {
	OUTDOOR("OUTDOOR", "외부"),
	INDOOR("INDOOR", "실내"),
	CANOPY("CANOPY", "캐노피"),
	ETC("ETC", "기타"),
	;

	private final String code;
	private final String value;

	InstallType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getValue() {
		return value;
	}
}
