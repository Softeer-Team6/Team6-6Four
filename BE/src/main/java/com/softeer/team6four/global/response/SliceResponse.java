package com.softeer.team6four.global.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class SliceResponse<T> {

    private final List<T> content;
    private final int size;
    private final boolean hasNext;

    @Builder
    private SliceResponse(List<T> content, int size, boolean hasNext) {
        this.content = content;
        this.size = size;
        this.hasNext = hasNext;
    }

    public static <T> SliceResponse<T> of(Slice<T> slice) {
        return new SliceResponse<>(slice.getContent(), slice.getContent().size(), slice.hasNext());
    }
}
