package com.softeer.team6four.domain.carbob.application;

import com.softeer.team6four.domain.carbob.application.request.CarbobRegistration;
import com.softeer.team6four.domain.carbob.domain.*;
import com.softeer.team6four.domain.carbob.infra.CarbobQrCreateEvent;
import com.softeer.team6four.domain.reservation.application.ReservationMapper;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.user.application.UserSearchService;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
	private final CarbobRepository carbobRepository;
	private final CarbobImageRepository carbobImageRepository;
	private final ReservationRepository reservationRepository;
	private final ApplicationEventPublisher eventPublisher;
	private final UserSearchService userSearchService;

	@Transactional
	public ResponseDto<Void> registerCarbob(Long userId, CarbobRegistration carbobRegistration) {
		User host = userSearchService.findUserByUserId(userId);

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

		Carbob newCarbob = carbobRepository.save(carbob);

		CarbobImage carbobImage = CarbobImage.builder()
			.imageUrl(carbobRegistration.getCarbobImgUrl())
			.carbob(newCarbob)
			.build();
		carbobImageRepository.save(carbobImage);

		List<ReservationLine> newReservationLines = makeReservationLines(carbobRegistration);
		Reservation savedReservation = reservationRepository.save(
			ReservationMapper.mapToSelfReservationEntity(carbob, host, newReservationLines));

		eventPublisher.publishEvent(new CarbobQrCreateEvent(newCarbob));

		return ResponseDto.map(HttpStatus.OK.value(), "카밥 등록에 성공했습니다.", null);
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
