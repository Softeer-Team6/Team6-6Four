package com.softeer.team6four.domain.reservation.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationLine {

	@Column(name = "reservation_time", nullable = false)
	private LocalDateTime reservationTime;

	@Builder
	public ReservationLine(LocalDateTime reservationTime) {
		this.reservationTime = reservationTime;
	}

}
