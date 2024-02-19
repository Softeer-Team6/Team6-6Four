package com.softeer.team6four.domain.carbob.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.carbob.domain.CarbobRepository;
import com.softeer.team6four.domain.carbob.infra.CarbobQrCreateEvent;
import com.softeer.team6four.global.infrastructure.s3.S3Service;
import com.softeer.team6four.global.util.CipherUtils;
import com.softeer.team6four.global.util.QrGeneratorUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarbobQrCreateEventListener {
	private final CarbobRepository carbobRepository;
	private final S3Service s3Service;
	private final CipherUtils cipherUtils;

	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleReservationCreatedEvent(CarbobQrCreateEvent event) {
		Carbob newCarbob = event.getCarbob();

		// carbob id 로 암호화
		String encryptedCarbobId = cipherUtils.encrypt(String.valueOf(newCarbob.getCarbobId()));

		// qr 만들고
		byte[] generatedQRCodeByteArray = QrGeneratorUtils.generateQRCodeByteArray(encryptedCarbobId);

		// s3 업로드해서
		String qrImageUrl = s3Service.saveCarbobQrAndReturnImageUrl(generatedQRCodeByteArray);

		// carbob 에 업데이트
		carbobRepository.updateImageByCarbobId(newCarbob.getCarbobId(), qrImageUrl);
	}

}
