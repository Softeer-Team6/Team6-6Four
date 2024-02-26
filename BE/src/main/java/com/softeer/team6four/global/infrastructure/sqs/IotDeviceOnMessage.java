package com.softeer.team6four.global.infrastructure.sqs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IotDeviceOnMessage {

	private final Long carbobId;
	private final Integer duration;

	@Override
	public String toString() {
		return "{\"carbobId\":" + carbobId + ",\"duration\":" + duration + "}";
	}
}
