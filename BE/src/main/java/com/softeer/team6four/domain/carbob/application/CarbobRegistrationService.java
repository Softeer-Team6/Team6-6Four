package com.softeer.team6four.domain.carbob.application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.softeer.team6four.domain.carbob.application.exception.CarbobException;
import com.softeer.team6four.domain.carbob.application.request.CarbobRegistration;
import com.softeer.team6four.domain.carbob.application.response.CarbobQr;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.carbob.domain.CarbobInfo;
import com.softeer.team6four.domain.carbob.domain.CarbobLocation;
import com.softeer.team6four.domain.carbob.domain.CarbobSpec;
import com.softeer.team6four.domain.carbob.domain.InstallType;
import com.softeer.team6four.domain.carbob.domain.LocationType;
import com.softeer.team6four.domain.carbob.domain.ChargerType;
import com.softeer.team6four.domain.carbob.domain.SpeedType;
import com.softeer.team6four.domain.carbob.domain.CarbobRepository;
import com.softeer.team6four.domain.reservation.application.ReservationMapper;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarbobRegistrationService {
    private final UserRepository userRepository;
    private final CarbobRepository carbobRepository;
    private final ReservationRepository reservationRepository;
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public ResponseDto<CarbobQr> registerCarbob(Long userId, CarbobRegistration carbobRegistration) {
        User host = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        CarbobLocation location = CarbobLocation.builder()
                .address(carbobRegistration.getAddress())
                .latitude(carbobRegistration.getLatitude())
                .longitude(carbobRegistration.getLongitude())
                .build();

        CarbobSpec spec = CarbobSpec.builder()
                .locationType(LocationType.valueOf(carbobRegistration.getLocationType()))
                .chargerType(ChargerType.valueOf(carbobRegistration.getChargeType()))
                .speedType(SpeedType.valueOf(carbobRegistration.getSpeedType()))
                .installType(InstallType.valueOf(carbobRegistration.getInstallType()))
                .build();

        CarbobInfo carbobInfo = CarbobInfo.builder()
                .description(carbobRegistration.getDescription())
                .speedType(SpeedType.valueOf(carbobRegistration.getSpeedType()))
                .fee(Integer.valueOf(carbobRegistration.getFeePer1kwh()))
                .build();

        Carbob carbob = Carbob.builder()
                .nickname(carbobRegistration.getCarbobNickname())
                .info(carbobInfo)
                .location(location)
                .spec(spec)
                .host(host)
                .build();

        try {
            // QR 코드 생성 및 S3에 업로드
            String qrImageUrl = generateQRCode(carbob);
            carbob.setCarbobQrImageUrl(qrImageUrl);

            // 카밥 저장
            carbobRepository.save(carbob);

            // 카밥 QR 코드 URL 생성
            CarbobQr carbobQr = new CarbobQr(carbob.getCarbobQrImageUrl());

            List<ReservationLine> newReservationLines = makeReservationLines(carbobRegistration);

            Reservation savedReservation = reservationRepository.save(ReservationMapper.mapToSelfReservationEntity(carbob, host, newReservationLines));

            return ResponseDto.map(HttpStatus.OK.value(), "카밥 등록에 성공했습니다.", carbobQr);
        } catch (IOException e) {
            throw new CarbobException(ErrorCode.CARBOB_REGISTRATION_FAILED);
        }
    }

    // QR 코드 생성
    private String generateQRCode(Carbob carbob) throws IOException {
        try {
            Long carbobId = carbob.getCarbobId(); // 카밥 객체로부터 carbobId를 가져옴
            String text = String.valueOf(carbobId);

            int width = 300;
            int height = 300;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // QR 코드 생성
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);

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
            byte[] bytes = baos.toByteArray();

            // S3에 업로드
            String fileName = "QR/" + UUID.randomUUID().toString() + ".png"; // QR 폴더 내에 이미지 저장
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            metadata.setContentType("image/png");

            amazonS3.putObject(bucket, fileName, new ByteArrayInputStream(bytes), metadata);

            // 업로드된 이미지의 URL 반환
            URL url = amazonS3.getUrl(bucket, fileName);
            return url.toString();
        } catch (Exception e) {
            log.error("QR 코드 생성 또는 S3 업로드 중 오류가 발생했습니다.", e);
            throw new CarbobException(ErrorCode.CARBOB_QR_FAILED);
        }
    }

    private List<ReservationLine> makeReservationLines(CarbobRegistration carbobRegistration) {
        List<ReservationLine> reservationLines = new ArrayList<>();
        LocalDateTime startDateTime = carbobRegistration.getStartDateTime();
        LocalDateTime endDateTime = carbobRegistration.getEndDateTime().minusMinutes(1);
        while (startDateTime.isBefore(endDateTime)) {
            ReservationLine line = new ReservationLine(startDateTime);
            reservationLines.add(line);
            startDateTime = startDateTime.plusHours(1);
        }
        return reservationLines;
    }
}
