package com.softeer.team6four.global.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WhitePathsConst {

    public static final String[] WHITE_PATHS = {
            "/", "/v1/user/auth/**"
    };
}
