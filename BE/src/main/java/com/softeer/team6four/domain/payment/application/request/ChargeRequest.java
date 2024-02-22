package com.softeer.team6four.domain.payment.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChargeRequest {
	private Integer chargePoint;

	public void setChargeRequest(Integer chargePoint){
		this.chargePoint = chargePoint;

	}
}

