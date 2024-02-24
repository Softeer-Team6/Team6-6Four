package com.softeer.team6four.domain.carbob.application;

import com.softeer.team6four.domain.carbob.application.response.MyCarbobDetailInfo;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.reservation.application.SelfUseTime;

public class CarbobMapper {
	public static MyCarbobDetailInfo mapMyCarbobDetailInfo(Carbob carbob, String carbobImageUrl, String qrImageUrl,
		SelfUseTime selfUseTime, Long totalIncomeByTargetId) {
		return MyCarbobDetailInfo.builder()
			.carbobId(carbob.getCarbobId())
			.nickname(carbob.getNickname())
			.imageUrl(carbobImageUrl)
			.qrImageUrl(qrImageUrl)
			.address(carbob.getLocation().getAddress())
			.feePerHour(String.valueOf(carbob.getInfo().getFeePerHour()).concat("원/kwh"))
			.chargerType(carbob.getSpec().getChargerType().getValue())
			.speedType(carbob.getSpec().getSpeedType().getValue())
			.installType(carbob.getSpec().getInstallType().getValue())
			.description(carbob.getInfo().getDescription())
			.selfUseTime(selfUseTime.toString())
			.carbobTotalIncome(totalIncomeByTargetId)
			.build();
	}

}
