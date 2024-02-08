package com.softeer.team6four.domain.reservation.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findReservationByCarbob_CarbobIdAndStateType(Long carbobId, StateType stateType);
    List<Reservation> findAllByCarbob_CarbobIdAndStateType(Long carbobId, StateType stateType);
}
