package com.softeer.team6four.domain.carbob.command.domain;

import com.softeer.team6four.global.infrastructure.database.CodeValue;

public enum ChargerType implements CodeValue {
    SLOW("SLOW", "완속"),
    AC3("AC3", "AC3상"),
    DESTINATION("DESTINATION", "급속"),
    ETC("ETC", "기타"),;

    private final String code;
    private final String value;

    ChargerType(String code, String value) {
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
