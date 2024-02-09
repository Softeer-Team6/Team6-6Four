package com.softeer.team6four.domain.reservation.presentation;

import com.softeer.team6four.domain.reservation.application.ReservationCreateService;
import com.softeer.team6four.domain.reservation.application.ReservationSearchService;
import com.softeer.team6four.domain.reservation.application.request.ReservationApply;
import com.softeer.team6four.domain.reservation.application.response.ReservationInfo;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationSearchService reservationSearchService;
    private final ReservationCreateService reservationCreateService;

    @GetMapping("/application/list")
    public ResponseDto<SliceResponse<ReservationInfo>> getMyReservationApplicationList
        (
            @RequestParam ReservationStateSortType sortType,
            @RequestParam(required = false) Long lastReservationId,
            @PageableDefault(size = 8) Pageable pageable
        )
    {
        // TODO : UserContextHold 에서 userId 가져오기
        Long userId = 3L;
        return reservationSearchService.getMyReservationApplicationList(userId, sortType, lastReservationId, pageable);
    }

    @PostMapping("/apply")
    public ResponseDto<Void> applyReservation
        (
            @RequestBody ReservationApply reservationApply
        )
    {
        // TODO : UserContextHold 에서 userId 가져오기
        Long userId = 2L;
        reservationCreateService.makeReservationToCarbobV1(userId, reservationApply);
        return ResponseDto.map(HttpStatus.OK.value(), "예약 신청이 완료되었습니다.", null);
    }
}
