package com.softeer.team6four.domain.reservation.infra;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softeer.team6four.domain.common.RepositoryTest;

@RepositoryTest
class DailyReservationRepositoryImplTest {

	@Autowired
	private DailyReservationRepositoryImpl dailyReservationRepositoryImpl;

	@Test
	@DisplayName("예약 불가능한 시간 조회")
	void findDailyReservationStatus() {
		//Given
		Long carbobId = 2L;
		String date = "2024-02-20";
		LocalDate localDate;
		localDate = LocalDate.parse(date);
		LocalDateTime startDateTime = localDate.atStartOfDay();
		LocalDateTime endDateTime = localDate.plusDays(1).atStartOfDay().minusSeconds(1);

		//Then
		List<LocalDateTime> checkDailyImpossibleTime = dailyReservationRepositoryImpl.
			findDailyReservationStatus(carbobId, startDateTime, endDateTime);

		//Then
		assertEquals(5, checkDailyImpossibleTime.size()); // DB의 불가능 시간만큼 나오는지 확인
	}

}
