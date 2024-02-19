package com.softeer.team6four.domain.reservation.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softeer.team6four.domain.reservation.ReservationConverterService;
import com.softeer.team6four.domain.reservation.application.ReservationCreateService;
import com.softeer.team6four.domain.reservation.application.ReservationSearchService;
import com.softeer.team6four.domain.reservation.application.ReservationUpdateService;
import com.softeer.team6four.domain.reservation.application.request.ReservationApply;
import com.softeer.team6four.domain.reservation.application.request.ReservationCheck;
import com.softeer.team6four.domain.reservation.application.request.ReservationFulfillRequest;
import com.softeer.team6four.domain.reservation.application.response.DailyReservationInfo;
import com.softeer.team6four.domain.reservation.application.response.QrVerification;
import com.softeer.team6four.domain.reservation.application.response.ReservationApplicationInfo;
import com.softeer.team6four.domain.reservation.application.response.ReservationCheckInfo;
import com.softeer.team6four.domain.reservation.application.response.ReservationFulfillResult;
import com.softeer.team6four.domain.reservation.application.response.ReservationInfo;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationSearchService reservationSearchService;
	private final ReservationCreateService reservationCreateService;
	private final ReservationConverterService reservationConverterService;
	private final ReservationUpdateService reservationUpdateService;

	@Auth
	@GetMapping("/application/list")
	public ResponseDto<SliceResponse<ReservationInfo>> getMyReservationApplicationList
		(
			@RequestParam ReservationStateSortType sortType,
			@RequestParam(required = false) Long lastReservationId,
			@PageableDefault(size = 8) Pageable pageable
		) {
		Long userId = UserContextHolder.get();
		return reservationSearchService.getMyReservationApplicationList(userId, sortType, lastReservationId, pageable);
	}

	@Auth
	@PostMapping("/apply")
	public ResponseDto<Void> applyReservation
		(
			@RequestBody ReservationApply reservationApply
		) {
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
		) {
		return reservationSearchService.getReservationList(carbobId, lastReservationId, pageable);
	}

	@Auth
	@GetMapping("/verification")
	public ResponseDto<QrVerification> verifyReservation
		(
			@RequestHeader String cipher
		) {
		Long userId = UserContextHolder.get();
		return reservationSearchService.verifyReservationByCipher(userId, cipher);
	}

	@Auth
	@PatchMapping("/check/{reservationId}")
	public ResponseDto<ReservationCheckInfo> checkReservation
		(
			@PathVariable Long reservationId,
			@RequestBody ReservationCheck reservationCheck
		) {
		Long hostUserId = UserContextHolder.get();
		return reservationConverterService.converterReservationState(hostUserId, reservationId, reservationCheck);
	}

	@Auth
	@GetMapping("/{carbobId}/daily")
	public ResponseDto<DailyReservationInfo> getdailyResrvationStatus
		(
			@PathVariable Long carbobId,
			@RequestParam(name = "date", defaultValue = "default") String date
		) {
		return reservationSearchService.getDailyReservationStatus(carbobId, date);
	}

	@Auth
	@PostMapping("/fulfillment")
	public ResponseDto<ReservationFulfillResult> fulfillReservation
		(
			@RequestBody ReservationFulfillRequest reservationFulfillRequest
		) {
		Long userId = UserContextHolder.get();
		return reservationUpdateService.fulfillReservationAndPay(userId, reservationFulfillRequest);
	}

}
