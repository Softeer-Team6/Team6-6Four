package com.softeer.team6four.domain.carbob.application.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbobRegistration  {
    private String description;
    private String carbobNickname;
    private String feePer1kwh;
    private String address;
    private Double latitude;
    private Double longitude;
    private String locationType;
    private String chargeType;
    private String speedType;
    private String installType;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public String getApplyDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        return startDateTime.format(formatter);
    }
}
