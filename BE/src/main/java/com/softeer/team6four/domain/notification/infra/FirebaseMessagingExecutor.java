package com.softeer.team6four.domain.notification.infra;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseMessagingExecutor {
	private final FirebaseMessaging firebaseMessaging;

	// Android 세팅
	private AndroidConfig androidConfig(String title, String body) {
		return AndroidConfig.builder()
			.setNotification(AndroidNotification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.build();
	}

	public void sendFCMNotificationSingle(String fcmToken, String title, String body) {
		Notification notification = Notification.builder()
			.setTitle(title).setBody(body).build();

		Message message = Message.builder()
			.setAndroidConfig(androidConfig(title, body))
			.setNotification(notification)
			.setToken(fcmToken)
			.build();
		try {
			firebaseMessaging.sendAsync(message);
		} catch (Exception e) {
			log.error("Failed to send FCM notification", e);
		}
	}

	public void sendFCMNotificationMulticast(List<String> fcmTokens, String title, String body) {
		Notification notification = Notification.builder()
			.setTitle(title).setBody(body).build();

		MulticastMessage multicastMessage = MulticastMessage.builder()
			.setNotification(notification)
			.setAndroidConfig(androidConfig(title, body))
			.addAllTokens(fcmTokens)
			.build();

		firebaseMessaging.sendMulticastAsync(multicastMessage);
	}
}
