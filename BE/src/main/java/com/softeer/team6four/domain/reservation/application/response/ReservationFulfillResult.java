package com.softeer.team6four.domain.reservation.application.response;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReservationFulfillResult {
    private final @NotNull String remainPoint;
    private final @NotNull String rentalPoint;
    private final @NotNull String reservationTime;
    private final @NotNull String address;
    private final @NotNull String estimatedChargeAmount;
}
