package com.softeer.team6four.domain.carbob.presentation;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.carbob.application.AroundCarbobSearchService;
import com.softeer.team6four.domain.carbob.application.CarbobRegistrationService;
import com.softeer.team6four.domain.carbob.application.CarbobSearchService;
import com.softeer.team6four.domain.carbob.application.request.CarbobRegistration;
import com.softeer.team6four.domain.carbob.application.response.*;
import com.softeer.team6four.global.annotation.Auth;
import com.softeer.team6four.global.filter.UserContextHolder;
import com.softeer.team6four.global.infrastructure.s3.S3Service;
import com.softeer.team6four.global.response.ListResponse;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = "/v1/carbob")
@RequiredArgsConstructor
public class CarbobController {
	private final CarbobSearchService carbobSearchService;
	private final CarbobRegistrationService carbobRegistrationService;
	private final AroundCarbobSearchService aroundCarbobSearchService;
	private final S3Service s3Service;

	@Auth
	@GetMapping(value = "/my")
	public ResponseDto<SliceResponse<MyCarbobSummary>> getMyCarbobList
		(
			@RequestParam MyCarbobSortType sortType,
			@RequestParam(required = false) Long lastCarbobId,
			@RequestParam(required = false) Long lastReservationCount,
			@PageableDefault(size = 6) Pageable pageable
		) {
		Long userId = UserContextHolder.get();
		SliceResponse<MyCarbobSummary> myCarbobList = carbobSearchService.findMyCarbobList(userId, sortType,
			lastCarbobId, lastReservationCount, pageable);
		return ResponseDto.map(HttpStatus.OK.value(), "내 차밥 리스트 조회에 성공했습니다.", myCarbobList);
	}

	@Auth
	@GetMapping(value = "/main")
	public ResponseDto<ListResponse<AroundCarbobListInfoSummary>> getAroundCarbobInfoSummaryList
		(
			@RequestParam @NotNull Double latitude,
			@RequestParam @NotNull Double longitude
		) {
		ListResponse<AroundCarbobListInfoSummary> aroundCarbobList = aroundCarbobSearchService.findAroundCarbobInfoSummaryList(
			latitude, longitude);
		return ResponseDto.map(HttpStatus.OK.value(), "반경 5KM 카밥 리스트가 반환되었습니다", aroundCarbobList);
	}

	@Auth
	@GetMapping(value = "/main/footer")
	public ResponseDto<ListResponse<AroundCarbobListInfo>> getAroundCarbobInfoList
		(
			@RequestParam @NotNull Double latitude,
			@RequestParam @NotNull Double longitude,
			@RequestParam(required = false, defaultValue = "SPEED") String sortType
		) {
		ListResponse<AroundCarbobListInfo> aroundCarbobList = aroundCarbobSearchService.findAroundCarbobInfoList(
			latitude, longitude, sortType);
		return ResponseDto.map(HttpStatus.OK.value(), "반경 5KM 카밥 리스트(footer용)가 반환되었습니다",
			aroundCarbobList);
	}

	@Auth
	@GetMapping(value = "/main/detail/{carbobId}")
	public ResponseDto<SpecificDetailCarbobInfo> getSpecificCarbobDetail
		(
			@PathVariable Long carbobId,
			@RequestParam @NotNull Double latitude,
			@RequestParam @NotNull Double longitude
		) {
		SpecificDetailCarbobInfo specificCarbob = aroundCarbobSearchService.findSpecificCarbobDetailInfo(latitude,
			longitude, carbobId);
		return ResponseDto.map(HttpStatus.OK.value(), "선택한 카밥 정보입니다", specificCarbob);
	}

	@Auth
	@GetMapping(value = "/detail/{carbobId}")
	public ResponseDto<MyCarbobDetailInfo> getMyCarbobDetail(@PathVariable Long carbobId) {
		Long userId = UserContextHolder.get();
		MyCarbobDetailInfo myCarbobDetailInfo = carbobSearchService.findMyCarbobDetailInfo(userId, carbobId);
		return ResponseDto.map(HttpStatus.OK.value(), "내 카밥 상세 조회에 성공했습니다.", myCarbobDetailInfo);
	}

	@Auth
	@PostMapping(value = "/registration")
	public ResponseDto<Void> registerCarbob
		(
			@RequestBody CarbobRegistration carbobRegistration
		) {
		Long userId = UserContextHolder.get();
		carbobRegistrationService.registerCarbob(userId, carbobRegistration);
		return ResponseDto.map(HttpStatus.OK.value(), "카밥 등록에 성공했습니다.", null);
	}

	@Auth
	@PostMapping(value = "/image")
	public ResponseDto<CarbobImgUrl> uploadCarbobImg
		(
			@RequestPart MultipartFile image
		) {
		String imageUrl = s3Service.saveCarbobAndReturnImageUrl(image);
		CarbobImgUrl carbobImgUrl = CarbobImgUrl.builder()
			.imgUrl(imageUrl)
			.build();
		return ResponseDto.map(HttpStatus.OK.value(), "S3에 이미지 등록 후 링크 반환이 성공했습니다.", carbobImgUrl);
	}
}

