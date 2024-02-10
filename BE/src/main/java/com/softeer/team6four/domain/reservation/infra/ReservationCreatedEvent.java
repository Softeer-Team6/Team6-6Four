package com.softeer.team6four.domain.reservation.infra;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreatedEvent {
    private final User guset;
    private final Carbob carbob;
}
