package com.softeer.team6four.domain.notification.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenRequest {
	private String fcmToken;
}
