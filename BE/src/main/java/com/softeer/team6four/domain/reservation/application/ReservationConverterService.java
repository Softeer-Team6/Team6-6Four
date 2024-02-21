package com.softeer.team6four.domain.reservation.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.reservation.application.exception.ReservationCheckStateTypeException;
import com.softeer.team6four.domain.reservation.application.exception.ReservationNotFoundException;
import com.softeer.team6four.domain.reservation.application.request.ReservationCheck;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.reservation.infra.ReservationCheckEvent;
import com.softeer.team6four.domain.user.application.UserSearchService;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationConverterService {
	private final ApplicationEventPublisher eventPublisher;
	private final ReservationRepository reservationRepository;
	private final UserSearchService userSearchService;

	@Transactional
	public void converterReservationState
		(Long hostUserId, Long reservationId, ReservationCheck reservationCheck) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

		if (!reservation.getStateType().equals(StateType.WAIT)) {
			throw new ReservationCheckStateTypeException(ErrorCode.INVALID_RESERVATION_CHECK_STATE);
		}

		reservation.changeStateType(reservationCheck.getStateType());

		User host = userSearchService.findUserByUserId(hostUserId);
		User guest = reservation.getGuest();
		Carbob carbob = reservation.getCarbob();

		eventPublisher.publishEvent(
			new ReservationCheckEvent(host, guest, carbob, reservationCheck.getStateType().getValue()));
	}
}

