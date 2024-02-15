package com.softeer.team6four.domain.carbob.application;

import com.softeer.team6four.domain.carbob.application.request.CarbobRegistration;
import com.softeer.team6four.domain.carbob.application.response.CarbobQr;
import com.softeer.team6four.domain.carbob.domain.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CarbobRegistrationService {
    private final UserRepository userRepository;
    private final CarbobRepository carbobRepository;
    private final ReservationRepository reservationRepository;

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

        carbobRepository.save(carbob);

        CarbobQr carbobQr = new CarbobQr(carbob.getQrImageUrl());

        List<ReservationLine> newReservationLines = makeReservationLines(carbobRegistration);

        Reservation savedReservation = reservationRepository.save(ReservationMapper.mapToSelfReservationEntity(carbob,host,newReservationLines));
        return ResponseDto.map(HttpStatus.OK.value(), "카밥 등록에 성공했습니다.", carbobQr);
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

