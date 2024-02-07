package com.softeer.team6four.global.infrastructure.database;

import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;
import jakarta.persistence.AttributeConverter;
import java.util.EnumSet;

public class EnumConverter<E extends Enum<E> & CodeValue> implements AttributeConverter<E, String> {

    private Class<E> clz;

    protected EnumConverter(Class<E> enumClass){
        this.clz = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return EnumSet.allOf(clz).stream()
                .filter(e->e.getCode().equals(dbData))
                .findAny()
                .orElseThrow(()-> new BusinessException(ErrorCode.ENUM_NOT_FOUND));
    }
}
