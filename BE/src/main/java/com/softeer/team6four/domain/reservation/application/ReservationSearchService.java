package com.softeer.team6four.domain.reservation.application;

import com.softeer.team6four.domain.reservation.application.response.ReservationApplicationInfo;
import com.softeer.team6four.domain.reservation.application.response.ReservationInfo;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.reservation.infra.ReservationRepositoryImpl;
import com.softeer.team6four.domain.reservation.presentation.ReservationStateSortType;
import com.softeer.team6four.global.response.ResponseDto;
import com.softeer.team6four.global.response.SliceResponse;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationSearchService {
    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryImpl reservationRepositoryImpl;

    public ResponseDto<SliceResponse<ReservationInfo>> getMyReservationApplicationList
        (
            Long userId,
            ReservationStateSortType sortType,
            Long lastReservationId,
            Pageable pageable
        )
    {
        Slice<ReservationInfo> reservationInfoList = reservationRepositoryImpl.findReservationInfoList(userId, sortType, lastReservationId, pageable);
        reservationInfoList.forEach(ReservationInfo::convertReservationTimeToStr);
        return ResponseDto.map(HttpStatus.OK.value(), "예약 내역 조회에 성공했습니다.", SliceResponse.of(reservationInfoList));
    }

    public ResponseDto<SliceResponse<ReservationApplicationInfo>> getReservationList
        (
            Long carbobId,
            Long lastReservationId,
            Pageable pageable
        )
    {
        Slice<ReservationApplicationInfo> reservationApplicationInfoList = reservationRepositoryImpl
            .findReservationApplicationInfoList(carbobId, lastReservationId, pageable);
        reservationApplicationInfoList.stream().forEach(ReservationApplicationInfo::convertReservationTimeToStr);

        return ResponseDto.map(HttpStatus.OK.value(), "예약 신청 내역 조회에 성공했습니다.", SliceResponse.of(reservationApplicationInfoList));
    }

    public SelfUseTime getSelfUseTime(Long carbobId) {
        Reservation reservation = reservationRepository
            .findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF)
            .orElse(null);

        return calculateSelfUseTime(reservation);
    }

    public List<Long> getUsedReservationIdListByCarbobId(Long carbobId) {
        return reservationRepository.findAllByCarbob_CarbobIdAndStateType(carbobId, StateType.USED)
            .stream()
            .map(Reservation::getReservationId)
            .collect(Collectors.toList());
    }

    private SelfUseTime calculateSelfUseTime(Reservation reservation) {
        if (reservation == null) {
            return SelfUseTime.builder().startTime(LocalDateTime.MIN).endTime(LocalDateTime.MIN).build();
        }
        List<ReservationLine> reservationLines = reservation.getReservationLines().stream()
            .sorted(Comparator.comparing(ReservationLine::getReservationTime))
            .toList();

        return SelfUseTime.builder()
            .startTime(reservationLines.get(0).getReservationTime())
            .endTime(reservationLines.get(reservation.getReservationLines().size() - 1).getReservationTime())
            .build();
    }
}
