package com.softeer.team6four.domain.carbob.application;

import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.application.response.SpecificDetailCarbobInfo;
import com.softeer.team6four.domain.carbob.infra.AroundCarbobRepository;
import com.softeer.team6four.global.response.ListResponse;
import com.softeer.team6four.global.response.ResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AroundCarbobSearchService {
	private final AroundCarbobRepository aroundCarbobRepository;

	public ListResponse<AroundCarbobListInfoSummary> findAroundCarbobInfoSummaryList
		(Double latitude, Double longitude) {
		Point point = createPoint(latitude, longitude);

		List<AroundCarbobListInfoSummary> aroundCarbobList = aroundCarbobRepository.findAllWithInCircleAreaSummary(
			point, 5000);
		return ListResponse.of(aroundCarbobList);
	}

	public ListResponse<AroundCarbobListInfo> findAroundCarbobInfoList
		(Double latitude, Double longitude, String sortType) {
		Point point = createPoint(latitude, longitude);

		List<AroundCarbobListInfo> aroundCarbobList = aroundCarbobRepository.findAllWithInCircleArea(point, 5000,
			sortType);
		return ListResponse.of(aroundCarbobList);
	}

	public SpecificDetailCarbobInfo findSpecificCarbobDetailInfo
		(Double latitude, Double longitude, Long carbobId) {
		Point point = createPoint(latitude, longitude);

		SpecificDetailCarbobInfo specificCarbob = aroundCarbobRepository.findSpecificCarbobDetailByCarbobId(point,
			carbobId);

		return specificCarbob;
	}

	public Point createPoint(Double latitude, Double longitude) {
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
		return geometryFactory.createPoint(new Coordinate(longitude, latitude));
	}

}

