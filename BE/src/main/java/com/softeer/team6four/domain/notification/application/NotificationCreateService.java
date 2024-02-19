package com.softeer.team6four.domain.notification.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.notification.domain.Notification;
import com.softeer.team6four.domain.notification.domain.NotificationRepository;
import com.softeer.team6four.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationCreateService {
	private final NotificationRepository notificationRepository;

	public void createNotification(User user, String message) {
		Notification notification = Notification.builder()
			.user(user)
			.message(message)
			.build();

		notificationRepository.save(notification);
	}
}
