package com.softeer.team6four.domain.carbob.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.application.response.SpecificDetailCarbobInfo;
import com.softeer.team6four.domain.carbob.infra.AroundCarbobRepository;
import com.softeer.team6four.global.response.ListResponse;

@ExtendWith(MockitoExtension.class)
class AroundCarbobSearchServiceTest {

	@Mock
	private AroundCarbobRepository aroundCarbobRepository;
	@InjectMocks
	private AroundCarbobSearchService aroundCarbobSearchService;

	@Test
	@DisplayName("주변 5KM 카밥 리스트 조회")
	void findAroundCarbobInfoSummaryList() {
		// Given
		double latitude = 37.1234;
		double longitude = 127.5678;
		List<AroundCarbobListInfoSummary> mockList = new ArrayList<>();

		when(aroundCarbobRepository.findAllWithInCircleAreaSummary(createPoint(latitude, longitude), 5000))
			.thenReturn(mockList);

		// When
		ListResponse<AroundCarbobListInfoSummary> result = aroundCarbobSearchService.findAroundCarbobInfoSummaryList(latitude, longitude);

		// Then
		assertEquals(mockList.size(), result.getContent().size());
	}

	@Test
	@DisplayName("주변 5KM 카밥 리스트 조회(footer)")
	void findAroundCarbobInfoList() {
		// Given
		double latitude = 37.1234;
		double longitude = 127.5678;
		String sortType = "DISTANCE";
		List<AroundCarbobListInfo> mockList = new ArrayList<>();

		when(aroundCarbobRepository.findAllWithInCircleArea(createPoint(latitude, longitude), 5000,sortType))
			.thenReturn(mockList);

		// When
		ListResponse<AroundCarbobListInfo> result = aroundCarbobSearchService.findAroundCarbobInfoList(latitude, longitude,sortType);

		// Then
		assertEquals(mockList.size(), result.getContent().size());
	}

	@Test
	@DisplayName("특정 카밥 상세 조회")
	void findSpecificCarbobDetailInfo() {
	}

	private Point createPoint(double latitude, double longitude) {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

		return point;
	}
}
