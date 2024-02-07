package com.softeer.team6four.domain.carbob.application;

import com.softeer.team6four.domain.carbob.application.response.MyCarbobSummary;
import com.softeer.team6four.domain.carbob.infra.CarbobRepositoryImpl;
import com.softeer.team6four.domain.carbob.presentation.MyCarbobSortType;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarbobSearchService {
    private final CarbobRepositoryImpl carbobRepositoryImpl;
    public ResponseDto<SliceResponse<MyCarbobSummary>> findMyCarbobList
        (
            Long userId, MyCarbobSortType sortType, Long lastCarbobId, Long lastReservationCount, Pageable pageable
        )
    {
        Slice<MyCarbobSummary> myCarbobSummarySlice = carbobRepositoryImpl.findCarbobSummaryByUserId(userId, sortType, lastCarbobId, lastReservationCount, pageable);
        return ResponseDto.map(HttpStatus.OK.value(), "내 카밥 조회에 성공했습니다.", SliceResponse.of(myCarbobSummarySlice));
    }
}
