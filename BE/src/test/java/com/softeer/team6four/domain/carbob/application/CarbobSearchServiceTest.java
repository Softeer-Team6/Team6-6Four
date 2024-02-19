package com.softeer.team6four.domain.carbob.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.softeer.team6four.domain.carbob.application.exception.CarbobNotFoundException;
import com.softeer.team6four.domain.carbob.application.exception.NotCarbobOwnerException;
import com.softeer.team6four.domain.carbob.application.response.MyCarbobDetailInfo;
import com.softeer.team6four.domain.carbob.application.response.MyCarbobSummary;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.carbob.domain.CarbobImage;
import com.softeer.team6four.domain.carbob.domain.CarbobImageRepository;
import com.softeer.team6four.domain.carbob.domain.CarbobInfo;
import com.softeer.team6four.domain.carbob.domain.CarbobLocation;
import com.softeer.team6four.domain.carbob.domain.CarbobRepository;
import com.softeer.team6four.domain.carbob.domain.CarbobSpec;
import com.softeer.team6four.domain.carbob.domain.ChargerType;
import com.softeer.team6four.domain.carbob.domain.InstallType;
import com.softeer.team6four.domain.carbob.domain.LocationType;
import com.softeer.team6four.domain.carbob.domain.SpeedType;
import com.softeer.team6four.domain.carbob.infra.CarbobRepositoryImpl;
import com.softeer.team6four.domain.carbob.presentation.MyCarbobSortType;
import com.softeer.team6four.domain.payment.application.PaymentSearchService;
import com.softeer.team6four.domain.reservation.application.ReservationSearchService;
import com.softeer.team6four.domain.reservation.application.SelfUseTime;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.exception.BusinessException;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.SliceResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("CarbobSearchServiceTest 테스트")
class CarbobSearchServiceTest {

	@Mock
	private CarbobImageRepository carbobImageRepository;
	@Mock
	private CarbobRepository carbobRepository;

	@Mock
	private PaymentSearchService paymentSearchService;

	@Mock
	private ReservationSearchService reservationSearchService;

	@Mock
	private CarbobRepositoryImpl carbobRepositoryImpl;

	@InjectMocks
	private CarbobSearchService carbobSearchService;

	private static Stream<Arguments> carbobListParameters() {
		return Stream.of(
			Arguments.of(MyCarbobSortType.LATEST, null, null),
			Arguments.of(MyCarbobSortType.POPULAR, null, null),
			Arguments.of(MyCarbobSortType.LATEST, 10L, null),
			Arguments.of(MyCarbobSortType.POPULAR, 10L, 5L)
		);
	}

	private static Slice<MyCarbobSummary> generateMyCarbobSummaries(Pageable pageable) {
		List<MyCarbobSummary> carbobSummaryList = List.of(
			new MyCarbobSummary(20L, "nickname_20", "qrImageUrl_20", 0L),
			new MyCarbobSummary(19L, "nickname_19", "qrImageUrl_19", 0L),
			new MyCarbobSummary(18L, "nickname_18", "qrImageUrl_18", 0L),
			new MyCarbobSummary(17L, "nickname_17", "qrImageUrl_17", 0L),
			new MyCarbobSummary(16L, "nickname_16", "qrImageUrl_16", 0L),
			new MyCarbobSummary(15L, "nickname_15", "qrImageUrl_15", 0L)
		);
		Slice<MyCarbobSummary> carbobSummarySlice = new SliceImpl<>(carbobSummaryList, pageable, true);
		return carbobSummarySlice;
	}

	private static Stream<Arguments> provideInvalidParameters() {
		return Stream.of(
			// userId가 null 이고 sortType 은 유효한 값
			Arguments.of(null, MyCarbobSortType.LATEST),
			// userId는 유효한 값이고 sortType 이 null
			Arguments.of(1L, null)
		);
	}

	@ParameterizedTest
	@MethodSource("carbobListParameters")
	@DisplayName("내 차밥 리스트 조회 테스트 - 성공케이스")
	void testFindMyCarbobListWithVariousParameters(MyCarbobSortType sortType, Long lastCarbobId,
		Long lastReservationCount) {
		// given
		Long userId = 1L;
		Pageable pageable = Pageable.ofSize(6);

		Slice<MyCarbobSummary> carbobSummarySlice = generateMyCarbobSummaries(pageable);

		when(carbobRepositoryImpl.findCarbobSummaryByUserId(userId, sortType, lastCarbobId, lastReservationCount,
			pageable))
			.thenReturn(carbobSummarySlice);

		// when
		SliceResponse<MyCarbobSummary> result = carbobSearchService.findMyCarbobList(userId, sortType, lastCarbobId,
			lastReservationCount, pageable);

		// then
		assertNotNull(result);
		assertEquals(carbobSummarySlice.getContent().size(), result.getContent().size());
		assertEquals(carbobSummarySlice.hasNext(), result.isHasNext());

		verify(carbobRepositoryImpl, times(1)).findCarbobSummaryByUserId(userId, sortType, lastCarbobId,
			lastReservationCount, pageable);
	}

	@ParameterizedTest
	@MethodSource("provideInvalidParameters")
	@DisplayName("유효하지 않은 파라미터에 대해 BusinessException 발생")
	void whenGivenInvalidParameters_thenThrowBusinessException(Long userId, MyCarbobSortType sortType) {
		Long lastCarbobId = null;
		Long lastReservationCount = null;
		Pageable pageable = Pageable.ofSize(6);

		BusinessException thrown = assertThrows(
			BusinessException.class,
			() -> carbobSearchService.findMyCarbobList(userId, sortType, lastCarbobId, lastReservationCount, pageable),
			"Expected findMyCarbobList to throw BusinessException, but it didn't"
		);

		assertEquals(ErrorCode.MISSING_REQUEST_PARAMETER, thrown.getErrorCode());
	}

	@Test
	@DisplayName("findMyCarbobDetailInfo 정상 케이스 테스트")
	void testFindMyCarbobDetailInfoSuccess() {
		// given
		Long userId = 1L;
		Long carbobId = 1L;
		Carbob carbob = createCarbob(createCarbobInfo(), createCarbobLocation(), createCarbobSpec());
		CarbobImage carbobImage = CarbobImage.builder()
			.carbob(carbob)
			.imageUrl("TEST_IMAGE_URL")
			.build();
		ReflectionTestUtils.setField(carbobImage, "carbobImageId", 1L);
		SelfUseTime selfUseTime = new SelfUseTime(
			LocalDateTime.of(LocalDate.of(2024, 2, 29), LocalTime.of(17, 0)),
			LocalDateTime.of(LocalDate.of(2024, 2, 29), LocalTime.of(23, 0)));

		when(carbobRepository.findById(carbobId)).thenReturn(Optional.of(carbob));
		when(carbobImageRepository.findCarbobImageByCarbob_CarbobId(carbobId)).thenReturn(Optional.of(carbobImage));
		when(reservationSearchService.getSelfUseTime(carbobId)).thenReturn(selfUseTime);
		when(paymentSearchService.getTotalIncomeByTargetId(carbobId)).thenReturn(1000L);

		// when
		MyCarbobDetailInfo detailInfo = carbobSearchService.findMyCarbobDetailInfo(userId, carbobId);

		// then
		assertNotNull(detailInfo);
		assertEquals(carbobImage.getImageUrl(), detailInfo.getImageUrl());
		assertEquals(selfUseTime.toString(), detailInfo.getSelfUseTime());
		assertEquals("TEST_IMAGE_URL", detailInfo.getImageUrl());
		assertEquals(1000L, detailInfo.getCarbobTotalIncome());
		assertEquals(carbobId, detailInfo.getCarbobId());

		verify(carbobRepository, times(1)).findById(carbobId);
		verify(carbobImageRepository, times(1)).findCarbobImageByCarbob_CarbobId(carbobId);
		verify(reservationSearchService, times(1)).getSelfUseTime(carbobId);
		verify(paymentSearchService, times(1)).getTotalIncomeByTargetId(carbobId);
	}

	@Test
	@DisplayName("findMyCarbobDetailInfo - 카밥이 없는 경우")
	void testFindMyCarbobDetailInfoWhenCarbobNotFound() {
		// given
		Long userId = 1L;
		Long carbobId = 1L;

		when(carbobRepository.findById(carbobId)).thenReturn(Optional.empty());

		// when
		CarbobNotFoundException thrown = assertThrows(
			CarbobNotFoundException.class,
			() -> carbobSearchService.findMyCarbobDetailInfo(userId, carbobId),
			"Expected findMyCarbobDetailInfo to throw CarbobNotFoundException, but it didn't"
		);

		assertEquals(ErrorCode.CARBOB_NOT_FOUND, thrown.getErrorCode());
		verify(carbobRepository, times(1)).findById(carbobId);
		verify(carbobImageRepository, never()).findCarbobImageByCarbob_CarbobId(carbobId);
		verify(reservationSearchService, never()).getSelfUseTime(carbobId);
		verify(paymentSearchService, never()).getTotalIncomeByTargetId(carbobId);
	}

	@Test
	@DisplayName("findMyCarbobDetailInfo - 카밥 주인이 아닌 경우")
	void testFindMyCarbobDetailInfoWhenNotCarbobOwner() {
		// given
		Long userId = 2L;
		Long carbobId = 1L;
		Carbob carbob = createCarbob(createCarbobInfo(), createCarbobLocation(), createCarbobSpec());
		ReflectionTestUtils.setField(carbob, "host", generateUser());

		when(carbobRepository.findById(carbobId)).thenReturn(Optional.of(carbob));

		// when
		NotCarbobOwnerException thrown = assertThrows(
			NotCarbobOwnerException.class,
			() -> carbobSearchService.findMyCarbobDetailInfo(userId, carbobId),
			"Expected findMyCarbobDetailInfo to throw NotCarbobOwnerException, but it didn't"
		);

		assertEquals(ErrorCode.NOT_CARBOB_OWNER, thrown.getErrorCode());
		verify(carbobRepository, times(1)).findById(carbobId);
		verify(carbobImageRepository, never()).findCarbobImageByCarbob_CarbobId(carbobId);
		verify(reservationSearchService, never()).getSelfUseTime(carbobId);
		verify(paymentSearchService, never()).getTotalIncomeByTargetId(carbobId);
	}

	private User generateUser() {
		User user = User.builder()
			.email("test1@test.com")
			.nickname("user1")
			.password("1234")
			.build();
		ReflectionTestUtils.setField(user, "userId", 1L);
		return user;
	}

	private Carbob createCarbob(CarbobInfo carbobInfo, CarbobLocation location, CarbobSpec spec) {
		Carbob carbob = Carbob.builder()
			.info(carbobInfo)
			.location(location)
			.spec(spec)
			.host(generateUser())
			.build();
		ReflectionTestUtils.setField(carbob, "carbobId", 1L);
		return carbob;
	}

	private CarbobInfo createCarbobInfo() {
		return CarbobInfo.builder()
			.description("테스트 설명")
			.speedType(SpeedType.KWH5)
			.fee(1000)
			.build();
	}

	private CarbobLocation createCarbobLocation() {
		return CarbobLocation.builder()
			.address("서울시 강남구")
			.latitude(37.123456)
			.longitude(127.123456)
			.build();
	}

	private CarbobSpec createCarbobSpec() {
		return CarbobSpec.builder()
			.locationType(LocationType.APARTMENT)
			.chargerType(ChargerType.AC3)
			.speedType(SpeedType.KWH5)
			.installType(InstallType.CANOPY)
			.build();
	}
}
