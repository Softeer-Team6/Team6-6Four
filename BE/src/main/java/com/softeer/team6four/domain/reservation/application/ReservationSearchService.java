package com.softeer.team6four.domain.reservation.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.reservation.application.response.DailyReservationInfo;
import com.softeer.team6four.domain.reservation.application.response.QrVerification;
import com.softeer.team6four.domain.reservation.application.response.ReservationApplicationInfo;
import com.softeer.team6four.domain.reservation.application.response.ReservationInfo;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.reservation.infra.DailyReservationRepositoryImpl;
import com.softeer.team6four.domain.reservation.infra.ReservationRepositoryImpl;
import com.softeer.team6four.domain.reservation.presentation.ReservationStateSortType;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import com.softeer.team6four.global.util.CipherUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationSearchService {
	private final CipherUtils cipherUtils;
	private final ReservationRepository reservationRepository;
	private final ReservationRepositoryImpl reservationRepositoryImpl;
	private final DailyReservationRepositoryImpl dailyReservationRepositoryImpl;

	public ResponseDto<SliceResponse<ReservationInfo>> getMyReservationApplicationList
		(
			Long userId,
			ReservationStateSortType sortType,
			Long lastReservationId,
			Pageable pageable
		) {
		Slice<ReservationInfo> reservationInfoList = reservationRepositoryImpl.findReservationInfoList(userId, sortType,
			lastReservationId, pageable);
		reservationInfoList.forEach(ReservationInfo::convertReservationTimeToStr);
		return ResponseDto.map(HttpStatus.OK.value(), "예약 내역 조회에 성공했습니다.", SliceResponse.of(reservationInfoList));
	}

	public ResponseDto<SliceResponse<ReservationApplicationInfo>> getReservationList
		(
			Long carbobId,
			Long lastReservationId,
			Pageable pageable
		) {
		Slice<ReservationApplicationInfo> reservationApplicationInfoList = reservationRepositoryImpl
			.findReservationApplicationInfoList(carbobId, lastReservationId, pageable);
		reservationApplicationInfoList.stream().forEach(ReservationApplicationInfo::convertReservationTimeToStr);

		return ResponseDto.map(HttpStatus.OK.value(), "예약 신청 내역 조회에 성공했습니다.",
			SliceResponse.of(reservationApplicationInfoList));
	}

	public ResponseDto<QrVerification> verifyReservationByCipher(Long userId, String cipher) {
		LocalDateTime now = LocalDateTime.now();
		Long decryptedCarbobId = Long.parseLong(cipherUtils.decrypt(cipher).split(":")[1]);

		Optional<Reservation> optionalReservation = reservationRepository
			.findAllByCarbob_CarbobIdAndGuest_UserIdAndStateType(decryptedCarbobId, userId, StateType.APPROVE)
			.stream()
			.filter(reservation -> {
				// 에약한 시간에 왔는지 체크
				LocalDateTime minTime = reservation.getReservationLines().stream()
					.map(ReservationLine::getReservationTime)
					.min(LocalDateTime::compareTo)
					.orElse(LocalDateTime.MAX);

				LocalDateTime maxTimePlus1Hour = reservation.getReservationLines().stream()
					.map(ReservationLine::getReservationTime)
					.max(LocalDateTime::compareTo)
					.orElse(LocalDateTime.MIN).plusHours(1);

				return now.isAfter(minTime) && now.isBefore(maxTimePlus1Hour);
			})
			.findFirst();

		if (optionalReservation.isPresent()) {
			return ResponseDto.map(
				HttpStatus.OK.value(),
				"QR 코드 검증에 성공했습니다.",
				QrVerification.builder()
					.reservationId(optionalReservation.get().getReservationId())
					.isVerified(true)
					.build());
		} else {
			return ResponseDto.map(
				HttpStatus.OK.value(),
				"유효한 예약이 없습니다.",
				QrVerification.builder().reservationId(0L).isVerified(false).build());
		}
	}

	public DailyReservationInfo getDailyReservationStatus(Long carbobId, String date) {

		LocalDate localDate;
		// 날짜를 입력하지 않았다면 현재 날짜로 설정
		if (date == null)
			localDate = LocalDate.now();
		else
			localDate = LocalDate.parse(date);

		// startDateTime-CurrentDateT00:00
		LocalDateTime startDateTime = localDate.atStartOfDay();
		// startDateTime-CurrentDateT23:59:59
		LocalDateTime endDateTime = localDate.plusDays(1).atStartOfDay().minusSeconds(1);
		// 불가능한 시간 확인
		List<LocalDateTime> checkDailyImpossibleTime = dailyReservationRepositoryImpl.
			findDailyReservationStatus(carbobId, startDateTime, endDateTime);

		boolean[] unavailableTimes = new boolean[24];

		for (LocalDateTime reservationTime : checkDailyImpossibleTime) {
			int hour = reservationTime.getHour();
			unavailableTimes[hour] = true;
		}

		return new DailyReservationInfo(unavailableTimes);
	}

	public SelfUseTime getSelfUseTime(Long carbobId) {
		Reservation reservation = reservationRepository
			.findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF)
			.orElse(null);

		return calculateSelfUseTime(reservation);
	}

	public List<Long> getUsedReservationIdListByCarbobId(Long carbobId) {
		return reservationRepository.findAllByCarbob_CarbobIdAndStateType(carbobId, StateType.USED)
			.stream()
			.map(Reservation::getReservationId)
			.collect(Collectors.toList());
	}

	private SelfUseTime calculateSelfUseTime(Reservation reservation) {
		if (reservation == null) {
			return SelfUseTime.builder().startTime(LocalDateTime.MIN).endTime(LocalDateTime.MIN).build();
		}
		List<ReservationLine> reservationLines = reservation.getReservationLines().stream()
			.sorted(Comparator.comparing(ReservationLine::getReservationTime))
			.toList();

		return SelfUseTime.builder()
			.startTime(reservationLines.get(0).getReservationTime())
			.endTime(reservationLines.get(reservation.getReservationLines().size() - 1).getReservationTime())
			.build();
	}
}
