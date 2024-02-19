package com.softeer.team6four.domain.carbob.domain;

import com.softeer.team6four.global.infrastructure.database.CodeValue;

public enum SpeedType implements CodeValue {
	KWH3("KWH3", "3"),
	KWH5("KWH5", "5"),
	KWH7("KWH7", "7"),
	KWH11("KWH11", "11"),
	;

	private final String code;
	private final String value;

	SpeedType(String code, String value) {
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
