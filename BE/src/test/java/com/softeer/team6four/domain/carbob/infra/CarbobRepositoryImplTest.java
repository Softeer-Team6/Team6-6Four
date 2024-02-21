package com.softeer.team6four.domain.carbob.infra;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.softeer.team6four.domain.carbob.application.response.MyCarbobSummary;
import com.softeer.team6four.domain.carbob.presentation.MyCarbobSortType;
import com.softeer.team6four.domain.common.RepositoryTest;

@RepositoryTest
class CarbobRepositoryImplTest {

	@Autowired
	private CarbobRepositoryImpl carbobRepositoryImpl;

	private Long userId;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		userId = 1L;
		pageable = Pageable.ofSize(6);
	}

	@Test
	@DisplayName("LATEST 정렬에서 Carbob 요약 정보 가져오기")
	void whenSortedByLatest_thenRetrieveCarbobSummaries() {
		testFindCarbobSummaryByUserId(MyCarbobSortType.LATEST, null, null, true);
	}

	@Test
	@DisplayName("LATEST 정렬에서 마지막 Carbob ID로 필터링하여 Carbob 요약 정보 가져오기")
	void whenSortedByLatestWithLastId_thenRetrieveCarbobSummaries() {
		testFindCarbobSummaryByUserId(MyCarbobSortType.LATEST, 7L, null, false);
	}

	@Test
	@DisplayName("POPULAR 정렬에서 Carbob 요약 정보 가져오기")
	void whenSortedByPopular_thenRetrieveCarbobSummaries() {
		testFindCarbobSummaryByUserId(MyCarbobSortType.POPULAR, null, null, true);
	}

	@Test
	@DisplayName("POPULAR 정렬에서 마지막 Carbob ID와 예약 수로 필터링하여 Carbob 요약 정보 가져오기")
	void whenSortedByPopularWithLastIdAndReservationCount_thenRetrieveCarbobSummaries() {
		testFindCarbobSummaryByUserId(MyCarbobSortType.POPULAR, 3L, 1L, false);
	}

	private void testFindCarbobSummaryByUserId(MyCarbobSortType sortType, Long lastCarbobId, Long lastReservationCount,
		boolean hasNextPage) {
		// when
		Slice<MyCarbobSummary> carbobSummarySlice = carbobRepositoryImpl.findCarbobSummaryByUserId(userId, sortType,
			lastCarbobId, lastReservationCount, pageable);

		// then
		assertNotNull(carbobSummarySlice, "Carbob 요약 정보는 null이 아니어야 합니다.");
		assertEquals(pageable.getPageSize(), carbobSummarySlice.getSize(), "페이지 크기가 일치해야 합니다.");
		assertEquals(hasNextPage, carbobSummarySlice.hasNext(), "다음 페이지 존재 여부가 예상과 일치해야 합니다.");

		if (sortType == MyCarbobSortType.LATEST) {
			for (int i = 0; i < carbobSummarySlice.getContent().size() - 1; i++)
				assertTrue(carbobSummarySlice.getContent().get(i).getCarbobId() > carbobSummarySlice.getContent()
					.get(i + 1)
					.getCarbobId(), "Carbob ID는 내림차순으로 정렬되어야 합니다.");
		} else {
			for (int i = 0; i < carbobSummarySlice.getContent().size() - 1; i++)
				assertTrue(
					carbobSummarySlice.getContent().get(i).getReservationCount() >= carbobSummarySlice.getContent()
						.get(i + 1)
						.getReservationCount(), "예약 수는 내림차순으로 정렬되어야 합니다.");
		}

		carbobSummarySlice.getContent().forEach(myCarbobSummary ->
			assertAll("Carbob 요약 정보 검증",
				() -> assertNotNull(myCarbobSummary.getCarbobId(), "Carbob ID는 null이 아니어야 합니다."),
				() -> assertNotNull(myCarbobSummary.getNickname(), "Nickname은 null이 아니어야 합니다."),
				() -> assertNotNull(myCarbobSummary.getImageUrl(), "이미지 URL은 null이 아니어야 합니다."),
				() -> assertNotNull(myCarbobSummary.getReservationCount(), "예약 수는 null이 아니어야 합니다.")
			)
		);
	}
}
