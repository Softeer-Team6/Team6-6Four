package com.softeer.team6four.domain.reservation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.reservation.application.exception.ReservationCheckStateTypeException;
import com.softeer.team6four.domain.reservation.application.exception.ReservationNotFoundException;
import com.softeer.team6four.domain.reservation.application.request.ReservationCheck;
import com.softeer.team6four.domain.reservation.application.response.ReservationCheckInfo;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.reservation.infra.ReservationCheckEvent;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationConverterService {
	private final ApplicationEventPublisher eventPublisher;
	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;

	@Transactional
	public ResponseDto<ReservationCheckInfo> converterReservationState
		(Long hostUserId, Long reservationId, ReservationCheck reservationCheck) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

		if (!reservation.getStateType().equals(StateType.WAIT)) {
			throw new ReservationCheckStateTypeException(ErrorCode.INVALID_RESERVATION_CHECK_STATE);
		}

		reservation.updateStateType(reservationCheck.getStateType());

		User host = userRepository.findById(hostUserId)
			.orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
		User guest = reservation.getGuest();
		Carbob carbob = reservation.getCarbob();

		ReservationCheckInfo reservationCheckInfo = ReservationCheckInfo.builder()
			.stateType(reservation.getStateType())
			.build();

		eventPublisher.publishEvent(
			new ReservationCheckEvent(host, guest, carbob, reservationCheckInfo.getStateType()));

		return ResponseDto.map(HttpStatus.OK.value(), "선택한 예약 승인/거절이 되었습니다", reservationCheckInfo);
	}
}

