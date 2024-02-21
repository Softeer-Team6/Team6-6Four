package com.softeer.team6four.domain.carbob.infra;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;

import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.application.response.SpecificDetailCarbobInfo;
import com.softeer.team6four.domain.common.RepositoryTest;

@RepositoryTest
class AroundCarbobRepositoryTest {

	@Autowired
	private AroundCarbobRepository aroundCarbobRepository;

	@Test
	@DisplayName("5km 근방 카밥 조회")
	void findAllWithInCircleAreaSummary() {
		//Given
		double centerLongitude = 126.9780;
		double centerLatitude = 37.5665;
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(centerLongitude, centerLatitude));
		int expectedSize = 212;
		int radius = 5000;

		//when
		List<AroundCarbobListInfoSummary> aroundCarbobList = aroundCarbobRepository.findAllWithInCircleAreaSummary(
			point, radius);

		//Then
		assertEquals(expectedSize, aroundCarbobList.size());
	}

	@Test
	@DisplayName("5km 근방 카밥 조회(footer)- 속도 순 정렬(내림차순)")
	void findAllWithInCircleArea_속도순_정렬() {
		//Given
		String sortType = "SPEED";
		double centerLongitude = 126.9780;
		double centerLatitude = 37.5665;
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(centerLongitude, centerLatitude));
		int radius = 5000;

		//When
		List<AroundCarbobListInfo> aroundCarbobList = aroundCarbobRepository.findAllWithInCircleArea(
			point, radius, sortType);

		// Then
		int previousSpeed = Integer.MAX_VALUE;
		for (AroundCarbobListInfo carbobInfo : aroundCarbobList) {
			int currentSpeed = extractSpeed(carbobInfo.getSpeedType());
			System.out.println(currentSpeed);
			assertTrue(previousSpeed >= currentSpeed);
			previousSpeed = currentSpeed;
		}
	}

	@Test
	@DisplayName("5km 근방 카밥 조회(footer)- 가격 순 정렬(오름차순)")
	void findAllWithInCircleArea_가격순_정렬() {
		//Given
		String sortType = "PRICE";
		double centerLongitude = 126.9780;
		double centerLatitude = 37.5665;
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(centerLongitude, centerLatitude));
		int radius = 5000;

		//When
		List<AroundCarbobListInfo> aroundCarbobList = aroundCarbobRepository.findAllWithInCircleArea(
			point, radius, sortType);

		// Then
		int previousSpeed = Integer.MIN_VALUE;
		for (AroundCarbobListInfo carbobInfo : aroundCarbobList) {
			int currentSpeed = extractFeePerHour(carbobInfo.getFeePerHour());
			assertTrue(previousSpeed <= currentSpeed);
			previousSpeed = currentSpeed;
		}
	}

	@Test
	@DisplayName("5km 근방 카밥 조회(footer)- 거리 순 정렬(오름차순)")
	void findAllWithInCircleArea_거리순_정렬() {
		//Given
		String sortType = "DISTANCE";
		double centerLongitude = 126.9780;
		double centerLatitude = 37.5665;
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(centerLongitude, centerLatitude));
		int radius = 5000;
		long expectedCarbobId = 149427L;

		//When
		List<AroundCarbobListInfo> aroundCarbobList = aroundCarbobRepository.findAllWithInCircleArea(
			point, radius, sortType);
		AroundCarbobListInfo firstCarbob = aroundCarbobList.get(0);
		long SortedFirstCarbobId = firstCarbob.getCarbobId();

		System.out.println(SortedFirstCarbobId);
		// Then
		assertEquals(expectedCarbobId,SortedFirstCarbobId);
	}

	@Test
	@DisplayName("특정 카밥 상세 조회")
	void findSpecificCarbobDetailByCarbobId() {
		//Given
		double centerLongitude = 126.9780;
		double centerLatitude = 37.5665;
		long carbobId = 149427L;
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(centerLongitude, centerLatitude));
		int expectedSize = 212;
		int radius = 5000;

		//when
		SpecificDetailCarbobInfo specificDetailCarbobInfo = aroundCarbobRepository.findSpecificCarbobDetailByCarbobId(
			point, carbobId);

		//Then
		assertNotNull(specificDetailCarbobInfo);
		assertEquals(carbobId, specificDetailCarbobInfo.getCarbobId());
	}

	private int extractSpeed(String speedType) {
		int indexOfKwh = speedType.indexOf("kwh");
		String speedNumber = speedType.substring(0, indexOfKwh).trim();
		return Integer.parseInt(speedNumber);
	}

	private int extractFeePerHour(String feePerHour) {
		// "원/kwh"가 포함된 부분의 인덱스 찾기
		int indexOfWon = feePerHour.indexOf("원/kwh");
		// "원/kwh" 이전까지의 문자열 추출
		String feeNumber = feePerHour.substring(0, indexOfWon).trim();
		// 추출된 숫자를 정수로 변환하여 반환
		return Integer.parseInt(feeNumber);
	}
}
