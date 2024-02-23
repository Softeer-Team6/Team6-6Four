package com.softeer.team6four.domain.carbob.application.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.firebase.database.annotations.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbobRegistration {
	private @NotNull String description;
	private @NotNull String carbobNickname;
	private @NotNull String feePer1kwh;
	private @NotNull String address;
	private @NotNull Double latitude;
	private @NotNull Double longitude;
	private @NotNull String locationType;
	private @NotNull String chargeType;
	private @NotNull String speedType;
	private @NotNull String installType;
	private @NotNull String carbobImgUrl;
	private @NotNull LocalDateTime startDateTime;
	private @NotNull LocalDateTime endDateTime;

	public String getApplyDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
		return startDateTime.format(formatter);
	}
}
