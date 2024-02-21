package com.softeer.team6four.global.infrastructure.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("S3Service 테스트")
class S3ServiceTest {

	@Value("${spring.cloud.aws.s3.defaultImgUrl}")
	private String defaultImageUrl;

	@InjectMocks
	private S3Service s3Service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("CarbobImg가 null 또는 비어있을 때 defaultImageUrl을 반환하는 테스트")
	void saveCarbobAndReturnDefaultImageUrlWhenCarbobImgIsEmptyTest() {
		// Given
		MultipartFile multipartFile = null;

		// When
		String imageUrl = s3Service.saveCarbobAndReturnImageUrl(multipartFile);

		// Then
		assertEquals(defaultImageUrl, imageUrl);
	}
}
