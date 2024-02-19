package com.softeer.team6four.domain.notification.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.softeer.team6four.domain.notification.domain.FcmToken;
import com.softeer.team6four.domain.notification.domain.FcmTokenRepository;
import com.softeer.team6four.domain.notification.infra.FirebaseMessagingExecutor;
import com.softeer.team6four.domain.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmSendService {
	private final FcmTokenRepository fcmTokenRepository;
	private final FirebaseMessagingExecutor firebaseMessagingExecutor;

	public void sendFcmNotification(User targetUser, String title, String message) {
		List<FcmToken> fcmTokens = fcmTokenRepository.findAllByUser_UserId(targetUser.getUserId());
		if (!fcmTokens.isEmpty()) {
			List<String> fcmTokenValueList = fcmTokens.stream().map(FcmToken::getFcmToken).collect(Collectors.toList());
			firebaseMessagingExecutor.sendFCMNotificationMulticast(fcmTokenValueList, title, message);
		}
	}
}
