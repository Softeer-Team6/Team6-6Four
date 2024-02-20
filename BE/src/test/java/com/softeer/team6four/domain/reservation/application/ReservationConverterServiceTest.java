package com.softeer.team6four.domain.reservation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.softeer.team6four.domain.reservation.ReservationConverterService;
import com.softeer.team6four.domain.reservation.application.exception.ReservationCheckStateTypeException;
import com.softeer.team6four.domain.reservation.application.exception.ReservationNotFoundException;
import com.softeer.team6four.domain.reservation.application.request.ReservationCheck;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.user.application.UserSearchService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationConverterServiceTest 테스트")
class ReservationConverterServiceTest {

	@Mock
	private ReservationRepository reservationRepository;
	@Mock
	private ApplicationEventPublisher eventPublisher;
	@Mock
	private UserSearchService userSearchService;
	@InjectMocks
	private ReservationConverterService reservationConverterService;

	@Test
	@DisplayName("예약 상태를 승인으로 변경")
	void converterReservationState_승인() {
		//Given
		Long hostUserId = 123L;
		Long reservationId = 456L;
		ReservationCheck reservationCheck = new ReservationCheck();
		reservationCheck.setStateType(StateType.APPROVE);
		Reservation reservation = Reservation.builder().stateType(StateType.WAIT).build();
		when(reservationRepository.findById(reservationId)).thenReturn(java.util.Optional.of(reservation));

		//When
		reservationConverterService.converterReservationState(hostUserId, reservationId, reservationCheck);

		//Then
		assertEquals(StateType.APPROVE, reservation.getStateType());
		verify(eventPublisher, never()).publishEvent(any());
	}

	@Test
	@DisplayName("예약 상태를 거절로 변경")
	void converterReservationState_거절() {
		//Given
		Long hostUserId = 123L;
		Long reservationId = 456L;
		ReservationCheck reservationCheck = new ReservationCheck();
		reservationCheck.setStateType(StateType.REJECT);
		Reservation reservation = Reservation.builder().stateType(StateType.WAIT).build();
		when(reservationRepository.findById(reservationId)).thenReturn(java.util.Optional.of(reservation));

		//When
		reservationConverterService.converterReservationState(hostUserId, reservationId, reservationCheck);

		//Then
		assertEquals(StateType.REJECT, reservation.getStateType());
		verify(eventPublisher, never()).publishEvent(any());
	}

	@Test
	@DisplayName("예약 ID가 없을 때 예외 발생")
	public void converterReservationState_예약_ID_Exception() {
		// Given
		Long hostUserId = 123L;
		Long reservationId = 456L;
		ReservationCheck reservationCheck = new ReservationCheck();
		reservationCheck.setStateType(StateType.REJECT);
		when(reservationRepository.findById(reservationId)).thenReturn(java.util.Optional.empty());

		// When, Then
		assertThrows(ReservationNotFoundException.class, () -> {
			reservationConverterService.converterReservationState(hostUserId, reservationId, reservationCheck);
		});
		verify(eventPublisher, never()).publishEvent(any());
	}

	@Test
	@DisplayName("예약 상태가 WAIT가 아닐때 예외 발생")
	public void converterReservationState_예약_상태가_WAIT가아닐때() {
		//Given
		Long hostUserId = 123L;
		Long reservationId = 456L;
		ReservationCheck reservationCheck = new ReservationCheck();
		reservationCheck.setStateType(StateType.REJECT);
		Reservation reservation = Reservation.builder().stateType(StateType.SELF).build();
		when(reservationRepository.findById(reservationId)).thenReturn(java.util.Optional.of(reservation));

		//When, Then
		assertThrows(ReservationCheckStateTypeException.class, () -> {
			reservationConverterService.converterReservationState(hostUserId, reservationId, reservationCheck);
		});
		verify(eventPublisher, never()).publishEvent(any());
	}

}
