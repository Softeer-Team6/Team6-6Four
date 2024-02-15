package com.softeer.team6four.domain.carbob.application.response;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CarbobImgUrl {
    private final @NotNull String imgUrl;
}
