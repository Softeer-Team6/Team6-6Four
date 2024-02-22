package com.softeer.team6four.domain.notification.application;

import com.softeer.team6four.domain.notification.domain.FcmToken;
import com.softeer.team6four.domain.notification.domain.FcmTokenRepository;
import com.softeer.team6four.domain.notification.application.request.FcmTokenRequest;
import com.softeer.team6four.domain.user.application.UserSearchService;
import com.softeer.team6four.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
	private final FcmTokenRepository fcmTokenRepository;
	private final UserSearchService userSearchService;

	@Transactional
	public void saveToken(Long userId, FcmTokenRequest fcmTokenRequest) {
		User user = userSearchService.findUserByUserId(userId);

		FcmToken fcmToken = FcmToken.builder()
			.fcmToken(fcmTokenRequest.getFcmToken())
			.user(user)
			.build();

		fcmTokenRepository.save(fcmToken);
	}
}
