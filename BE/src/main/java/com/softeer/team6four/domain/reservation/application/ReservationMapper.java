package com.softeer.team6four.domain.reservation.application;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.reservation.application.response.ReservationFulfillResult;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.user.domain.User;
import java.util.List;

public class ReservationMapper {
    public static Reservation mapToReservationEntity(Carbob carbob, User user, List<ReservationLine> newReservationLines) {
        return Reservation.builder()
            .totalFee(carbob.getInfo().getFeePerHour() * newReservationLines.size())
            .stateType(StateType.WAIT)
            .carbob(carbob)
            .guest(user)
            .reservationLines(newReservationLines)
            .build();
    }

    public static ReservationFulfillResult mapToReservationFulfillResult
        (
            Carbob carbob, Reservation reservation,
            Long userCurrentTotalPoint, String reservationTime, String estimatedChargeAmount
        )
    {
        return ReservationFulfillResult.builder()
            .remainPoint((userCurrentTotalPoint - reservation.getTotalFee()) + "원")
            .rentalPoint(reservation.getTotalFee() + "원")
            .address(carbob.getLocation().getAddress())
            .reservationTime(reservationTime)
            .estimatedChargeAmount(estimatedChargeAmount)
            .build();
    }

}
