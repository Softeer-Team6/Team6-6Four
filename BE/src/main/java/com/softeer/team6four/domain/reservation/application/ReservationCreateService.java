package com.softeer.team6four.domain.reservation.application;

import com.softeer.team6four.domain.carbob.application.exception.CarbobNotFoundException;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.carbob.domain.CarbobRepository;
import com.softeer.team6four.domain.reservation.application.exception.InvalidReservationTimeLinesException;
import com.softeer.team6four.domain.reservation.application.request.ReservationApply;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.infra.ReservationCreatedEvent;
import com.softeer.team6four.domain.reservation.infra.ReservationRepositoryImpl;
import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ErrorCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationCreateService {
    private final ApplicationEventPublisher eventPublisher;

    private final UserRepository userRepository;
    private final CarbobRepository carbobRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryImpl reservationRepositoryImpl;

    @Transactional
    public Long makeReservationToCarbobV1(Long userId, ReservationApply reservationApply) {
        if(isNotAvailableReservationTimeLines(reservationApply)) {
            throw new InvalidReservationTimeLinesException(ErrorCode.INVALID_RESERVATION_TIME_LINES);
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Carbob carbob = carbobRepository.findById(reservationApply.getCarbobId())
            .orElseThrow(() -> new CarbobNotFoundException(ErrorCode.CARBOB_NOT_FOUND));

        List<ReservationLine> newReservationLines = makeReservationLines(reservationApply);

        boolean isDuplicatedTimeLines = reservationRepositoryImpl.existsByCarbobIdAndReservationLines(reservationApply.getCarbobId(), newReservationLines);
        if(isDuplicatedTimeLines) {
            throw new InvalidReservationTimeLinesException(ErrorCode.INVALID_RESERVATION_TIME_LINES);
        }

        Reservation savedReservation = reservationRepository.save(ReservationMapper.mapToReservationEntity(carbob, user, newReservationLines));

        eventPublisher.publishEvent(new ReservationCreatedEvent(user, carbob));

        return savedReservation.getReservationId();
    }

    private boolean isNotAvailableReservationTimeLines(ReservationApply reservationApply) {
        return
            reservationApply.getStartDateTime().isAfter(reservationApply.getEndDateTime())
            ||
            reservationApply.getStartDateTime().isBefore(LocalDateTime.now());
    }

    private List<ReservationLine> makeReservationLines(ReservationApply reservationApply) {
        List<ReservationLine> reservationLines = new ArrayList<>();
        LocalDateTime startDateTime = reservationApply.getStartDateTime();
        LocalDateTime endDateTime = reservationApply.getEndDateTime().minusMinutes(1);
        while (startDateTime.isBefore(endDateTime)) {
            ReservationLine line = new ReservationLine(startDateTime);
            reservationLines.add(line);
            startDateTime = startDateTime.plusHours(1);
        }
        return reservationLines;
    }
}
