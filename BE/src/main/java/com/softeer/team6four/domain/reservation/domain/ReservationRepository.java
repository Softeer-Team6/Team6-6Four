package com.softeer.team6four.domain.reservation.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findReservationByCarbob_CarbobIdAndStateType(Long carbobId, StateType stateType);
    List<Reservation> findAllByCarbob_CarbobIdAndStateType(Long carbobId, StateType stateType);
    List<Reservation> findAllByCarbob_CarbobIdAndGuest_UserIdAndStateType(Long carbobId, Long userId, StateType stateType);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.carbob WHERE r.reservationId = :reservationId")
    Optional<Reservation> findReservationWithCarbobById(@Param("reservationId") Long reservationId);
}
