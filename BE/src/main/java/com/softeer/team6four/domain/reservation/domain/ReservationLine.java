package com.softeer.team6four.domain.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationLine {

    @Column(name = "reservation_time")
    private LocalDateTime reservationTime;

    @Builder
    public ReservationLine(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

}
