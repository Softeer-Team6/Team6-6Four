package com.softeer.team6four.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QrGeneratorUtils 테스트")
class QrGeneratorUtilsTest {

	@Test
	@DisplayName("유효한 문자열(암호화된 carbobid가 넘어온 경우)일 때 QR 코드 생성")
	void generateQRCodeByteArray_ValidInput_Success() {
		// Given
		String input = "Test123";

		// When
		byte[] qrCodeByteArray = QrGeneratorUtils.generateQRCodeByteArray(input);

		// Then
		assertNotNull(qrCodeByteArray);
		assertTrue(qrCodeByteArray.length > 0);
	}
}
