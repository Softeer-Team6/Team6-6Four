package com.softeer.team6four.domain.carbob.domain;

import com.softeer.team6four.global.infrastructure.database.CodeValue;

public enum LocationType implements CodeValue {
    HOUSE("HOUSE", "단독주택"),
    VILLA("VILLA", "빌라"),
    APARTMENT("APARTMENT", "아파트"),
    ETC("ETC","기타"),;

    private final String code;
    private final String value;

    LocationType(String code, String value) {
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
