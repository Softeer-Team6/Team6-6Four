package com.softeer.team6four.global.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;

@Configuration
public class FCMConfig {
	@Bean
	FirebaseMessaging firebaseMessaging() {
		try {
			ClassPathResource resource = new ClassPathResource("fbServiceAccountKey.json");

			InputStream refreshToken = resource.getInputStream();

			FirebaseApp firebaseApp = null;
			List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

			if (firebaseAppList != null && !firebaseAppList.isEmpty()) {
				for (FirebaseApp app : firebaseAppList) {
					if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
						firebaseApp = app;
					}
				}
			} else {
				FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(refreshToken))
					.build();

				firebaseApp = FirebaseApp.initializeApp(options);
			}
			return FirebaseMessaging.getInstance(firebaseApp);
		} catch (IOException e) {
			throw BusinessException.builder()
				.errorCode(ErrorCode.INTERNAL_SERVER_ERROR).build();
		}
	}
}
