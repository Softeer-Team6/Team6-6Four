package com.softeer.team6four.domain.payment.domain;

import com.softeer.team6four.global.infrastructure.database.CodeValue;

public enum PayType implements CodeValue {
	CHARGE("CHARGE", "포인트 충전"),
	USE("USE", "포인트 사용"),
	INCOME("INCOME", "포인트 수입"),
	;

	private final String code;
	private final String value;

	PayType(String code, String value) {
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
