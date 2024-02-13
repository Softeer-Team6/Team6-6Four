package com.softeer.team6four.domain.carbob.application;

import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.infra.AroundCarbobRepositoryImpl;
import com.softeer.team6four.domain.carbob.presentation.CarbobListStateSortType;
import com.softeer.team6four.global.response.ListResponse;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AroundCarbobSearchService {
    private final AroundCarbobRepositoryImpl  aroundCarbobRepositoryImpl;

    public ResponseDto<ListResponse<AroundCarbobListInfoSummary>> findAroundCarbobInfoSummaryList
            (Double latitude, Double longitude)
    {
        List<AroundCarbobListInfoSummary> aroundCarbobList =  aroundCarbobRepositoryImpl.findAroundCarbobSummaryByMyPosition(latitude,longitude);
        return ResponseDto.map(HttpStatus.OK.value(), "반경 5KM 카밥 리스트가 반환되었습니다", ListResponse.of(aroundCarbobList));
    }

    public ResponseDto<ListResponse<AroundCarbobListInfo>> findAroundCarbobInfoList
            (Double latitude, Double longitude, CarbobListStateSortType sortType)
    {
        List<AroundCarbobListInfo> aroundCarbobList =  aroundCarbobRepositoryImpl.findAroundCarbobByMyPosition(latitude,longitude,sortType);
        return ResponseDto.map(HttpStatus.OK.value(), "반경 5KM 카밥 리스트(footer용)가 반환되었습니다", ListResponse.of(aroundCarbobList));
    }

}

