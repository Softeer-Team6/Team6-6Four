package com.softeer.team6four.domain.carbob.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbobInfo {

	@Column(nullable = false)
	private String description;

	@Column(name = "fee_per_hour", nullable = false)
	private Integer feePerHour;

	@Builder
	public CarbobInfo(String description, SpeedType speedType, Integer fee) {
		this.description = description;
		this.feePerHour = calculateFee(speedType, fee);
	}

	private Integer calculateFee(SpeedType speedType, Integer fee) {
		return Integer.parseInt(speedType.getValue()) * fee;
	}

}
