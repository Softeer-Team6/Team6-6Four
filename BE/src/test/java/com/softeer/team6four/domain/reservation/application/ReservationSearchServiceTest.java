package com.softeer.team6four.domain.reservation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.reservation.infra.DailyReservationRepositoryImpl;
import com.softeer.team6four.domain.reservation.infra.ReservationRepositoryImpl;
import com.softeer.team6four.global.util.CipherUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationSearchServiceTest 테스트")
class ReservationSearchServiceTest {

	@Mock
	private CipherUtils cipherUtils;
	@Mock
	private ReservationRepository reservationRepository;
	@Mock
	private ReservationRepositoryImpl reservationRepositoryImpl;
	@Mock
	private DailyReservationRepositoryImpl dailyReservationRepositoryImpl;

	@InjectMocks
	private ReservationSearchService reservationSearchService;

	@Test
	@DisplayName("자기사용 예약 시간이 없을 때 조회 성공")
	void getSelfUseTime_ReservationIsNull() {
		// given
		Long carbobId = 1L;
		when(reservationRepository.findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF))
			.thenReturn(Optional.empty());

		// when
		SelfUseTime result = reservationSearchService.getSelfUseTime(carbobId);

		// then
		assertNotNull(result);
		assertEquals(LocalDateTime.MIN, result.getStartTime());
		assertEquals(LocalDateTime.MIN, result.getEndTime());

		verify(reservationRepository, times(1)).findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF);
	}

	@Test
	@DisplayName("자기사용 예약 시간이 있을 때 조회 성공")
	void getSelfUseTime_ReservationIsNotNull() {
		// given
		Long carbobId = 1L;
		LocalDateTime startTime = LocalDateTime.of(2023, 1, 1, 12, 0);
		LocalDateTime endTime = LocalDateTime.of(2023, 1, 1, 14, 0);
		ReservationLine startLine = new ReservationLine(startTime);
		ReservationLine endLine = new ReservationLine(endTime);
		Reservation reservation = Reservation.builder()
			.guest(null)
			.carbob(null)
			.reservationLines(Arrays.asList(startLine, endLine))
			.stateType(StateType.SELF)
			.build();

		when(reservationRepository.findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF))
			.thenReturn(Optional.of(reservation));

		// when
		SelfUseTime result = reservationSearchService.getSelfUseTime(carbobId);

		// then
		assertNotNull(result);
		assertEquals(startTime, result.getStartTime());
		assertEquals(endTime, result.getEndTime());

		verify(reservationRepository, times(1)).findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF);
	}
}
