package com.softeer.team6four.domain.reservation.application.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationFulfillRequest {
    private Long reservationId;
}
