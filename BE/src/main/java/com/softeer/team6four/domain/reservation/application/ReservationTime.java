package com.softeer.team6four.domain.reservation.application;

import com.google.firebase.database.annotations.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationTime {
    private final @NotNull LocalDateTime startTime;
    private final @NotNull LocalDateTime endTime;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String date = startTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        String hours = startTime.format(formatter) + " ~ " + endTime.plusMinutes(59).format(formatter);
        return date + " " + hours;
    }
}
