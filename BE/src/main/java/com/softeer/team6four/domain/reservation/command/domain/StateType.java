package com.softeer.team6four.domain.reservation.command.domain;


import com.softeer.team6four.global.infrastructure.database.CodeValue;

public enum StateType implements CodeValue {
    SELF("SELF", "본인이용"),
    WAIT("WAIT", "대기"),
    APPROVE("APPROVE", "승인"),
    REJECT("REJECT", "거절"),
    USED("USED", "사용"),
    ;

    private final String code;
    private final String value;

    StateType(String code, String value) {
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
