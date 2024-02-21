package com.softeer.team6four.domain.carbob.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softeer.team6four.domain.common.RepositoryTest;

@RepositoryTest
class CarbobRepositoryTest {

	@Autowired
	private CarbobRepository carbobRepository;

	@Test
	@DisplayName("Carbob 이미지 URL 업데이트")
	void givenCarbobId_whenUpdateImage_thenImageUpdated() {
		// given
		Long existingCarbobId = 1L;
		Carbob originalCarbob = carbobRepository.findById(existingCarbobId)
			.orElseThrow(() -> new AssertionError("Carbob 엔티티가 존재해야 합니다."));
		assertNotEquals("newImageUrl", originalCarbob.getQrImageUrl());

		// when
		carbobRepository.updateImageByCarbobId(existingCarbobId, "newImageUrl");

		// then
		Carbob updatedCarbob = carbobRepository.findById(existingCarbobId)
			.orElseThrow(() -> new AssertionError("Carbob 엔티티가 존재해야 합니다."));
		assertEquals("newImageUrl", updatedCarbob.getQrImageUrl());
	}
}
