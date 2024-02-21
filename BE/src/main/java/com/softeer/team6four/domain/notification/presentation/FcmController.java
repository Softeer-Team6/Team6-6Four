package com.softeer.team6four.domain.notification.presentation;

import com.softeer.team6four.domain.notification.application.FcmService;
import com.softeer.team6four.domain.notification.request.FcmTokenRequest;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/fcm")
@RequiredArgsConstructor
public class FcmController {
	private final FcmService fcmService;

	@Auth
	@PostMapping(value = "/my/token")
	public ResponseDto<Void> saveToken(@RequestBody FcmTokenRequest fcmTokenRequest) {
		Long userId = UserContextHolder.get();
		fcmService.saveToken(userId, fcmTokenRequest);
		return ResponseDto.map(HttpStatus.OK.value(), "FCM 토큰 저장에 성공했습니다.", null);
	}
}

