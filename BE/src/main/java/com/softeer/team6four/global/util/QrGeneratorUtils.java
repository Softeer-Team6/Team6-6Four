package com.softeer.team6four.global.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Slf4j
public class QrGeneratorUtils {

    public static byte[] generateQRCodeByteArray(String encryptedCarbobId) {
        try {
            int width = 300;
            int height = 300;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // QR 코드 생성
            BitMatrix bitMatrix = new MultiFormatWriter().encode(encryptedCarbobId, BarcodeFormat.QR_CODE, width, height);

            // 이미지로 변환
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // ByteArrayOutputStream에 이미지 쓰기
            ImageIO.write(bufferedImage, "png", baos);

            // byte 배열로 변환
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("QR 코드 생성 중 오류가 발생했습니다.", e);
            throw new BusinessException(ErrorCode.CARBOB_QR_FAILED);
        }
    }
}
