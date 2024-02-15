package com.softeer.team6four.domain.reservation.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class DailyReservationInfo {
    boolean[] dailyBookedTimeCheck = new boolean[24];

    public void setUnavailableTime(int hour, boolean booked){
        dailyBookedTimeCheck[hour] = booked;
    }

}
