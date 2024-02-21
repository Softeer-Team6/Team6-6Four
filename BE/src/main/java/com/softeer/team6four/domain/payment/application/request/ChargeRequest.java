package com.softeer.team6four.domain.payment.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ChargeRequest {
	private Integer chargePoint;
}

