package com.softeer.team6four.domain.reservation.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SelfUseTime {
	private final @NotNull LocalDateTime startTime;
	private final @NotNull LocalDateTime endTime;

	@Override
	public String toString() {
		if (startTime == LocalDateTime.MIN && endTime == LocalDateTime.MIN) {
			return "미설정";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		return startTime.format(formatter) + " ~ " + endTime.plusMinutes(59).format(formatter);
	}
}
