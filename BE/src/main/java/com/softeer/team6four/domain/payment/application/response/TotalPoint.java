package com.softeer.team6four.domain.payment.application.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TotalPoint {
	private final Integer totalPoint;
}
