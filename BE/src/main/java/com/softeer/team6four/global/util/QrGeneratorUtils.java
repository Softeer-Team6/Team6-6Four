package com.softeer.team6four.global.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * QR 코드를 생성하는 유틸리티 클래스입니다.
 */
@Slf4j
public class QrGeneratorUtils {

	/**
	 * 주어진 문자열을 암호화하여 QR 코드의 바이트 배열을 생성합니다.
	 *
	 * @param encryptedCarbobId QR 코드로 인코딩할 암호화된 문자열
	 * @return QR 코드의 바이트 배열
	 * @throws BusinessException QR 코드 생성 중 오류가 발생한 경우
	 */
	public static byte[] generateQRCodeByteArray(String encryptedCarbobId) {
		try {
			int width = 300;
			int height = 300;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			BitMatrix bitMatrix = new MultiFormatWriter().encode(encryptedCarbobId, BarcodeFormat.QR_CODE, width,
				height);

			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
				}
			}

			ImageIO.write(bufferedImage, "png", baos);

			return baos.toByteArray();
		} catch (Exception e) {
			log.error("QR 코드 생성 중 오류가 발생했습니다.", e);
			throw new BusinessException(ErrorCode.CARBOB_QR_FAILED);
		}
	}
}
