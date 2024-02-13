package com.softeer.team6four.domain.reservation.application.response;

import com.google.firebase.database.annotations.NotNull;
import com.softeer.team6four.domain.reservation.application.ReservationTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationApplicationInfo {
    private final @NotNull Long reservationId;
    private final @NotNull String carbobNickname;
    private final @NotNull ReservationTime reservationTime;
    private @NotNull String rentalDate;
    private @NotNull String rentalTime;
    private final @NotNull String address;
    private final @NotNull String guestNickname;
    private final @NotNull Integer totalFee;

    public void convertReservationTimeToStr() {
        String[] timeSplit = this.reservationTime.toString().split(" ");
        this.rentalDate = timeSplit[0];
        this.rentalTime = timeSplit[1];
    }

}
