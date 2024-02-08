package com.softeer.team6four.domain.reservation.application;

import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationSearchService {
    private final ReservationRepository reservationRepository;

    public SelfUseTime getSelfUseTime(Long carbobId) {
        Reservation reservation = reservationRepository
            .findReservationByCarbob_CarbobIdAndStateType(carbobId, StateType.SELF)
            .orElse(null);

        return calculateSelfUseTime(reservation);
    }

    public List<Long> getUsedReservationIdList(Long carbobId) {
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
