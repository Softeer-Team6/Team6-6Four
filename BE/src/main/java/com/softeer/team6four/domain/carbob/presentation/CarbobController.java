package com.softeer.team6four.domain.carbob.presentation;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.carbob.application.AroundCarbobSearchService;
import com.softeer.team6four.domain.carbob.application.CarbobSearchService;
import com.softeer.team6four.domain.carbob.application.response.*;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.response.ListResponse;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import com.softeer.team6four.domain.carbob.presentation.CarbobListStateSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1/carbob")
@RequiredArgsConstructor
public class CarbobController {
    private final CarbobSearchService carbobSearchService;
    private final AroundCarbobSearchService aroundCarbobSearchService;

    @Auth
    @GetMapping(value = "/my")
    public ResponseDto<SliceResponse<MyCarbobSummary>> getMyCarbobList
        (
            @RequestParam MyCarbobSortType sortType,
            @RequestParam(required = false) Long lastCarbobId,
            @RequestParam(required = false) Long lastReservationCount,
            @PageableDefault(size = 6) Pageable pageable
        )
    {
        Long userId = UserContextHolder.get();
        return carbobSearchService.findMyCarbobList(userId, sortType, lastCarbobId, lastReservationCount, pageable);
    }
    @Auth
    @GetMapping(value = "/main")
    public ResponseDto<ListResponse<AroundCarbobListInfoSummary>> getAroundCarbobInfoSummaryList
            (
                    @RequestParam @NotNull Double latitude,
                    @RequestParam @NotNull Double longitude
            )
    {
        return aroundCarbobSearchService.findAroundCarbobInfoSummaryList(latitude,longitude);
    }
    @Auth
    @GetMapping(value = "/main/footer")
    public ResponseDto<ListResponse<AroundCarbobListInfo>> getAroundCarbobInfoList
            (
                    @RequestParam @NotNull Double latitude,
                    @RequestParam @NotNull Double longitude,
                    @RequestParam(required = false, defaultValue = "SPEED") CarbobListStateSortType sortType
            )
    {
        return aroundCarbobSearchService.findAroundCarbobInfoList(latitude,longitude,sortType);
    }

    @Auth
    @GetMapping(value = "/main/detail/{carbobId}")
    public ResponseDto<SpecificDetailCarbobInfo> getSpecificCarbobDetail
            (
                    @PathVariable Long carbobId,
                    @RequestParam @NotNull Double latitude,
                    @RequestParam @NotNull Double longitude
            )
    {
        return aroundCarbobSearchService.findSpecificCarbobDetailInfo(latitude, longitude, carbobId);
    }


    @Auth
    @GetMapping(value = "/detail/{carbobId}")
    public ResponseDto<MyCarbobDetailInfo> getMyCarbobDetail(@PathVariable Long carbobId) {
        Long userId = UserContextHolder.get();
        return carbobSearchService.findMyCarbobDetailInfo(userId, carbobId);
    }
}

