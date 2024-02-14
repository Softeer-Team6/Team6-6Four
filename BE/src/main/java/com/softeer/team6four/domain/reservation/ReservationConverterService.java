package com.softeer.team6four.domain.reservation;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.reservation.application.ReservationMapper;
import com.softeer.team6four.domain.reservation.application.exception.ReservationNotFoundException;
import com.softeer.team6four.domain.reservation.application.request.ReservationCheck;
import com.softeer.team6four.domain.reservation.application.response.ReservationCheckInfo;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.infra.ReservationCreatedEvent;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationConverterService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseDto<ReservationCheckInfo> converterReservationState
            (Long userId, Long reservationId, ReservationCheck reservationCheck)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.updateStateType(reservationCheck.getStateType());

        Carbob carbob = reservation.getCarbob();


        return ResponseDto.map(HttpStatus.OK.value(),"선택한 예약 승인/거절이 되었습니다",
                ReservationCheckInfo.builder().stateType(reservation.getStateType()).build());
    }
}

