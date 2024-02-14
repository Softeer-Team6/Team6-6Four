package com.softeer.team6four.domain.reservation.presentation;

import com.softeer.team6four.domain.reservation.ReservationConverterService;
import com.softeer.team6four.domain.reservation.application.ReservationCreateService;
import com.softeer.team6four.domain.reservation.application.ReservationSearchService;
import com.softeer.team6four.domain.reservation.application.request.ReservationApply;
import com.softeer.team6four.domain.reservation.application.request.ReservationCheck;
import com.softeer.team6four.domain.reservation.application.response.QrVerification;
import com.softeer.team6four.domain.reservation.application.response.ReservationApplicationInfo;
import com.softeer.team6four.domain.reservation.application.response.ReservationCheckInfo;
import com.softeer.team6four.domain.reservation.application.response.ReservationInfo;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import com.softeer.team6four.global.util.CipherUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final CipherUtils cipherUtils;
    private final ReservationSearchService reservationSearchService;
    private final ReservationCreateService reservationCreateService;
    private final ReservationConverterService reservationConverterService;

    @Auth
    @GetMapping("/application/list")
    public ResponseDto<SliceResponse<ReservationInfo>> getMyReservationApplicationList
        (
            @RequestParam ReservationStateSortType sortType,
            @RequestParam(required = false) Long lastReservationId,
            @PageableDefault(size = 8) Pageable pageable
        )
    {
        Long userId = UserContextHolder.get();
        return reservationSearchService.getMyReservationApplicationList(userId, sortType, lastReservationId, pageable);
    }

    @Auth
    @PostMapping("/apply")
    public ResponseDto<Void> applyReservation
        (
            @RequestBody ReservationApply reservationApply
        )
    {
        Long userId = UserContextHolder.get();
        reservationCreateService.makeReservationToCarbobV1(userId, reservationApply);
        return ResponseDto.map(HttpStatus.OK.value(), "예약 신청이 완료되었습니다.", null);
    }

    @Auth
    @GetMapping("/list/{carbobId}")
    public ResponseDto<SliceResponse<ReservationApplicationInfo>> getReservationList
        (
            @PathVariable Long carbobId,
            @RequestParam(required = false) Long lastReservationId,
            @PageableDefault(size = 8) Pageable pageable
        )
    {
        return reservationSearchService.getReservationList(carbobId, lastReservationId, pageable);
    }

    @Auth
    @GetMapping("/verification")
    public ResponseDto<QrVerification> verifyReservation
        (
            @RequestHeader String cipher
        )
    {
        Long userId = UserContextHolder.get();
        return reservationSearchService.verifyReservationByCipher(userId, cipher);
    }
    @Auth
    @PatchMapping("/check/{reservationId}")
    public ResponseDto<ReservationCheckInfo> checkReservation
            (
                    @PathVariable Long reservationId,
                    @RequestBody ReservationCheck reservationCheck
            )
    {
        Long userId = UserContextHolder.get();
        return reservationConverterService.converterReservationState(reservationId, reservationCheck);
    }
}
